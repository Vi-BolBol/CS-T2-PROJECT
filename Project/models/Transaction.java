package models;

import db.DatabaseObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable record of a completed payment.
 *
 * ENCAPSULATION: all fields are private and final (immutable after creation).
 * INHERITANCE:   extends DatabaseObject.
 */
public class Transaction extends DatabaseObject {

    // ── Allowed statuses ──────────────────────────────────────────────────────
    public enum TxStatus { PENDING, COMPLETED, FAILED, REFUNDED }

    // ── Immutable fields ──────────────────────────────────────────────────────
    private final String          orderId;
    private final double          foodAmount;
    private final double          tableAmount;
    private final double          discountPercent;
    private final String          paymentMethod;
    private final LocalDateTime   transactionDate;
    private final TxStatus        status;

    // ── Constructor ───────────────────────────────────────────────────────────

    public Transaction(String orderId, double foodAmount, double tableAmount,
                       double discountPercent, String paymentMethod) {
        super();
        if (orderId == null || orderId.isBlank())
            throw new IllegalArgumentException("Order ID cannot be empty.");
        if (foodAmount <= 0)
            throw new IllegalArgumentException("Food amount must be > 0.");
        if (tableAmount < 0)
            throw new IllegalArgumentException("Table charge cannot be negative.");
        if (discountPercent < 0 || discountPercent > 100)
            throw new IllegalArgumentException("Discount must be 0–100 %.");
        if (paymentMethod == null || paymentMethod.isBlank())
            throw new IllegalArgumentException("Payment method cannot be empty.");

        this.orderId         = orderId;
        this.foodAmount      = foodAmount;
        this.tableAmount     = tableAmount;
        this.discountPercent = discountPercent;
        this.paymentMethod   = paymentMethod;
        this.transactionDate = LocalDateTime.now();
        this.status          = TxStatus.COMPLETED;
    }

    // ── Computed values ───────────────────────────────────────────────────────

    public double getSubtotal()    { return foodAmount + tableAmount; }
    public double getDiscountAmt() { return getSubtotal() * discountPercent / 100.0; }
    public double getFinalTotal()  { return getSubtotal() - getDiscountAmt(); }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String        getOrderId()       { return orderId; }
    public double        getFoodAmount()    { return foodAmount; }
    public double        getTableAmount()   { return tableAmount; }
    public double        getDiscountPct()   { return discountPercent; }
    public String        getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getTransactionDate(){ return transactionDate; }
    public TxStatus      getStatus()        { return status; }
    public boolean       isSuccessful()     { return status == TxStatus.COMPLETED; }

    public String getFormattedDate() {
        return transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ── DatabaseObject contract ───────────────────────────────────────────────

    @Override
    public String getSummary() { return buildReceipt(); }

    public String buildReceipt() {
        final String RESET  = "\u001B[0m", CYAN  = "\u001B[36m", GREEN  = "\u001B[32m",
                     YELLOW = "\u001B[33m", BLUE  = "\u001B[34m", BOLD   = "\u001B[1m",
                     GRAY   = "\u001B[90m", RED   = "\u001B[31m";

        String statusColor = switch (status) {
            case COMPLETED -> GREEN;
            case PENDING   -> YELLOW;
            case FAILED    -> RED;
            default        -> GRAY;
        };

        return CYAN + BOLD + "═══════════════════════════════════════════════════════════════\n"
             + "                  TRANSACTION RECEIPT\n"
             + "═══════════════════════════════════════════════════════════════\n" + RESET
             + String.format(BLUE + " %-22s" + RESET + " : " + YELLOW + "$%8.2f%n" + RESET, "Food Amount",   foodAmount)
             + String.format(BLUE + " %-22s" + RESET + " : " + YELLOW + "$%8.2f%n" + RESET, "Table Charge",  tableAmount)
             + GRAY + "───────────────────────────────────────────────────────────────\n" + RESET
             + String.format(BOLD + " %-22s" + RESET + " : " + GREEN + BOLD + "$%8.2f%n" + RESET, "Sub-total",    getSubtotal())
             + String.format(" %-22s"        + RESET + " : " + RED   + "-$%7.2f%n"      + RESET, "Discount (" + (int)discountPercent + "%)", getDiscountAmt())
             + GRAY + "───────────────────────────────────────────────────────────────\n" + RESET
             + String.format(BOLD + " %-22s" + RESET + " : " + GREEN + BOLD + "$%8.2f%n" + RESET, "TOTAL PAID",   getFinalTotal())
             + GRAY + "───────────────────────────────────────────────────────────────\n" + RESET
             + String.format(BLUE + " %-22s" + RESET + " : %s%n",       "Payment Method", paymentMethod)
             + String.format(BLUE + " %-22s" + RESET + " : %s%n",       "Date",           getFormattedDate())
             + String.format(BLUE + " %-22s" + RESET + " : %s%s%s%n",   "Status",  statusColor + BOLD, status, RESET)
             + CYAN + "═══════════════════════════════════════════════════════════════\n" + RESET;
    }
}
