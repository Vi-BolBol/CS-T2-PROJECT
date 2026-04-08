package models;

import db.DatabaseObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a customer order.
 *
 * ENCAPSULATION: mutable state (items, status) is private and modified only
 *                through validated methods.
 * INHERITANCE:   extends DatabaseObject for id / timestamp.
 */
public class Order extends DatabaseObject {

    /** Allowed order types. */
    public enum OrderType { TABLE, ONLINE }

    /** Allowed lifecycle states. */
    public enum OrderStatus { PENDING, PREPARING, READY, COMPLETED, CANCELLED }

    // ── Fields ────────────────────────────────────────────────────────────────

    private final OrderType        orderType;
    private       String           tableId;        // null for ONLINE orders
    private final List<Food>       foodItems   = new ArrayList<>();
    private final List<Integer>    quantities  = new ArrayList<>();
    private       double           totalAmount = 0.0;
    private final LocalDateTime    orderDate   = LocalDateTime.now();
    private       OrderStatus      status      = OrderStatus.PENDING;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Order(OrderType orderType) {
        super();
        this.orderType = orderType;
    }

    // ── Mutation methods ──────────────────────────────────────────────────────

    /**
     * Adds a food item to the order.
     * ENCAPSULATION: the internal list is never directly handed out.
     */
    public void addItem(Food food, int qty, double unitPrice) {
        if (food == null)   throw new IllegalArgumentException("Food cannot be null.");
        if (qty <= 0)       throw new IllegalArgumentException("Quantity must be > 0.");
        if (unitPrice <= 0) throw new IllegalArgumentException("Unit price must be > 0.");

        foodItems.add(food);
        quantities.add(qty);
        totalAmount += unitPrice * qty;
    }

    /**
     * Reverses the charge for a food item and removes it from the order.
     * Used during order cancellation to restore food stock.
     */
    public void removeItem(Food food) {
        int index = foodItems.indexOf(food);
        if (index < 0) return;
        totalAmount -= food.getPrice() * quantities.get(index);
        foodItems.remove(index);
        quantities.remove(index);
    }

    public void setTableId(String tableId) {
        if (orderType == OrderType.TABLE && (tableId == null || tableId.isBlank()))
            throw new IllegalArgumentException("Table ID cannot be empty for TABLE orders.");
        this.tableId = tableId;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    /**
     * Advances the order to the next logical status in the lifecycle:
     *   PENDING -> PREPARING -> READY -> COMPLETED
     * Returns false if the order is already in a terminal state (COMPLETED or CANCELLED).
     */
    public boolean advanceStatus() {
        status = switch (status) {
            case PENDING    -> OrderStatus.PREPARING;
            case PREPARING  -> OrderStatus.READY;
            case READY      -> OrderStatus.COMPLETED;
            default         -> null;   // COMPLETED or CANCELLED – cannot advance
        };
        return status != null;
    }

    /** Returns the status that advanceStatus() would move to, or null if terminal. */
    public OrderStatus peekNextStatus() {
        return switch (status) {
            case PENDING   -> OrderStatus.PREPARING;
            case PREPARING -> OrderStatus.READY;
            case READY     -> OrderStatus.COMPLETED;
            default        -> null;
        };
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public OrderType       getOrderType()  { return orderType; }
    public String          getTableId()    { return tableId; }
    public double          getTotalAmount(){ return totalAmount; }
    public LocalDateTime   getOrderDate()  { return orderDate; }
    public OrderStatus     getStatus()     { return status; }
    public int             getItemCount()  { return foodItems.size(); }

    /** Unmodifiable view – callers cannot mutate internal state. */
    public List<Food>    getFoodItems()  { return Collections.unmodifiableList(foodItems); }
    public List<Integer> getQuantities() { return Collections.unmodifiableList(quantities); }

    public boolean isTableOrder()  { return orderType == OrderType.TABLE; }
    public boolean isOnlineOrder() { return orderType == OrderType.ONLINE; }

    public String getFormattedDate() {
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ── DatabaseObject contract ───────────────────────────────────────────────

    @Override
    public String getSummary() { return buildSummary(); }

    /** Builds a coloured CLI receipt string. */
    public String buildSummary() {
        final String RESET  = "\u001B[0m", CYAN = "\u001B[36m", GREEN  = "\u001B[32m",
                     YELLOW = "\u001B[33m", BLUE = "\u001B[34m", BOLD   = "\u001B[1m",
                     RED    = "\u001B[31m", GRAY = "\u001B[90m";

        StringBuilder sb = new StringBuilder();
        String header = isTableOrder() ? "TABLE ORDER" : "ONLINE ORDER";
        sb.append(CYAN).append(BOLD).append("\n ORDER SUMMARY – ").append(header).append("\n\n").append(RESET);

        if (foodItems.isEmpty()) {
            sb.append(GRAY).append("  No items yet.\n").append(RESET);
        } else {
            sb.append(BLUE).append(" #   Item                          Qty    Price     Subtotal\n")
              .append("───────────────────────────────────────────────────────\n").append(RESET);
            for (int i = 0; i < foodItems.size(); i++) {
                Food f  = foodItems.get(i);
                int  q  = quantities.get(i);
                sb.append(String.format(GRAY + "%2d" + RESET + "  %-25s  " + GREEN + "%3d" + RESET
                        + " x " + YELLOW + "$%5.2f" + RESET + "  =  " + BOLD + YELLOW
                        + "$%6.2f" + RESET + "%n",
                        i + 1,
                        f.getName().length() > 25 ? f.getName().substring(0, 22) + "..." : f.getName(),
                        q, f.getPrice(), f.getPrice() * q));
            }
        }

        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(BOLD).append(" Items: ").append(RESET).append(foodItems.size())
          .append("   ").append(BOLD).append("Grand Total: ").append(RESET)
          .append(GREEN).append(BOLD).append(String.format("$%.2f", totalAmount)).append(RESET).append("\n");

        String statusColor = switch (status) {
            case COMPLETED  -> GREEN;
            case PENDING    -> YELLOW;
            case CANCELLED  -> RED;
            default         -> BLUE;
        };
        sb.append(GRAY).append(" Status: ").append(statusColor).append(BOLD)
          .append(status).append(RESET).append(GRAY).append("   Date: ")
          .append(RESET).append(getFormattedDate()).append("\n");

        return sb.toString();
    }
}
