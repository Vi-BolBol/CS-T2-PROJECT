import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    String transactionId;
    String orderId;
    double amount;
    String paymentMethod;
    LocalDateTime transactionDate;
    String status;

    public Transaction(String transactionId, String orderId, double amount, String paymentMethod) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionDate = LocalDateTime.now();
        this.status = "completed";
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return transactionDate.format(formatter);
    }

    public String getTransactionReceipt() {
        System.out.println("");
      return String.format("══════════ RECEIPT ══════════\n" +
                           "Transaction ID: %s\n" +
                           "Order ID: %s\n" +
                           "Amount: $%.2f\n" +
                           "Payment Method: %s\n" +
                           "Status: %s\n" +
                           "Date: %s\n" +
                           "═════════ SUNFLOWER ═════════",
            transactionId, orderId, amount, paymentMethod, status, getFormattedDate());
    }
}