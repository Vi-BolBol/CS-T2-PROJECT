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
        setOrderId(orderId);
        setOrderType(orderType);
        
        if (orderType.equalsIgnoreCase("Online")) {
            this.tableId = null;
        }
        
        this.foodItems = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        setStatus("Pending");
    }

    public Order(String orderId, String orderType, String tableId) {
        setOrderId(orderId);
        setOrderType(orderType);
        
        if (orderType.equalsIgnoreCase("Table")) {
            setTableId(tableId);
        } else {
            this.tableId = null;
        }
        
        this.foodItems = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        setStatus("Pending");
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getTableId() {
        return tableId;
    }

    public List<String> getFoodItems() {
        return new ArrayList<>(foodItems);
    }

    public List<Integer> getQuantities() {
        return new ArrayList<>(quantities);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId;
    }

    public void setOrderType(String orderType) {
        if (orderType == null || orderType.trim().isEmpty()) {
            throw new IllegalArgumentException("Order type cannot be empty");
        }

        String upperType = orderType.toUpperCase();
        if (!upperType.equals("ONLINE") && !upperType.equals("TABLE")) {
            throw new IllegalArgumentException("Must be: Online or Table");
        }
        this.orderType = orderType;
    }

    public void setTableId(String tableId) {
        if (this.orderType != null && this.orderType.equalsIgnoreCase("Table")) {
            if (tableId == null || tableId.trim().isEmpty()) {
                throw new IllegalArgumentException("Table ID cannot be empty for Table orders");
            }
        }
        this.tableId = tableId;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("PENDING") && !upperStatus.equals("PREPARING") && 
            !upperStatus.equals("READY") && !upperStatus.equals("COMPLETED") && 
            !upperStatus.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid. Must be: Pending, Preparing, Ready, Completed, or Cancelled");
        }
        this.status = status;
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

    public void removeItem(String foodId, double price) {
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
        String tableInfo = isTableOrder() ? " - Table " + tableId : " - Online Order";
        return String.format("Order %s%s - Items: %d - Total: $%.2f - Status: %s - Date: %s",
            orderId, tableInfo, getItemCount(), totalAmount, status, getFormattedDate());
    }
}