package models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String orderType; 
    private String tableId;  

    private List<String> foodItems;  
    private List<Integer> quantities; 

    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;

    public Order(String orderId, String orderType) {
        this.orderId = orderId;
        this.orderType = orderType;
        
        if (orderType.equalsIgnoreCase("Online")) {
            this.tableId = null;
        }
        
        this.foodItems = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        this.status = "Pending";
    }

    public Order(String orderId, String orderType, String tableId) {
        this.orderId = orderId;
        this.orderType = orderType;
        
        if (orderType.equalsIgnoreCase("Table")) {
            this.tableId = tableId;
        } else {
            this.tableId = null;
        }
        
        this.foodItems = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        this.status = "Pending";
    }

    public void addOrder(String foodId, int quantity, double price) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        foodItems.add(foodId);
        quantities.add(quantity);
        totalAmount += (price * quantity);
    }

    public void removeOrder(String foodId, double price) {
        int index = foodItems.indexOf(foodId);
        if (index != -1) {
            int quantity = quantities.get(index);
            totalAmount -= (price * quantity);
            foodItems.remove(index);
            quantities.remove(index);
        }
    }

    public int getItemCount() {
        return foodItems.size();
    }

    public double getTotalPrice(){
        return totalAmount;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orderDate.format(formatter);
    }

    public boolean isOnlineOrder() {
        return orderType.equalsIgnoreCase("Online");
    }

    public boolean isTableOrder() {
        return orderType.equalsIgnoreCase("Table");
    }

    public String getOrderSummary() {
        String orderType = isTableOrder() ? "Table " + tableId : "Online";

        String statusText = switch (status != null ? status.toLowerCase() : "") {
            case "pending"   -> "Pending";
            case "preparing" -> "Preparing";
            case "ready"     -> "Ready for pickup";
            case "completed" -> "Completed";
            case "cancelled" -> "Cancelled";
            case "delivered" -> "Delivered";
            default          -> status != null ? status : "Unknown";
        };

        return String.format(
            "Order %-10s   %-18s   Items: %3d    Total: $%8.2f    Status: %-12s    %s",
            orderId,
            orderType,
            getItemCount(),
            totalAmount,
            statusText,
            getFormattedDate()
        );
    }
}