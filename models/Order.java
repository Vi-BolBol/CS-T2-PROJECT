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
        this.status = "Complete";
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
        this.status = "Complete";
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
    
    public String getOrderId(){
        return this.orderId;
    }

    @Override
    public String toString() {
        return orderId + " " + orderType + " " + tableId + " " + getItemCount() + " " + totalAmount + " " + getFormattedDate() + " " + status;
    }
}