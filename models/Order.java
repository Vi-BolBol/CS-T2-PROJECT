package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseObject;

public class Order extends DatabaseObject{
    
    private String orderType; 
    private String tableId;   
    private List<Food> foodItems;  
    private List<Integer> quantities; 
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;

    public Order(String orderType) {
        
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

    public Order(){

        this.tableId = null;
        
        this.foodItems = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        setStatus("Pending");
        
    }

    public Order(String orderType, String tableId) {
        
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

    public String getOrderType() {
        return orderType;
    }

    public String getTableId() {
        return tableId;
    }

    public List<Food> getFoodItems() {
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

    public void setOrderType(String orderType) {
        if (orderType == null || orderType.trim().isEmpty()) {
            throw new IllegalArgumentException("Order type cannot be empty");
        }

        String upperType = orderType.toUpperCase();
        if (!upperType.equals("ONLINE") && !upperType.equals("TABLE")) {
            throw new IllegalArgumentException("Must be: Online or Table");
        }
        this.orderType = orderType;

        if(upperType.equals("TABLE")){
            setTableId("12345");
        }
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

    public void addItem(Food food, int quantity, double price) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        foodItems.add(food);
        quantities.add(quantity);
        totalAmount += (price * quantity);
    }

    public void removeItem(Food food, double price) {
        int index = foodItems.indexOf(food);
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
        // ANSI color codes (only work in terminals that support them)
        String RESET   = "\u001B[0m";
        String CYAN    = "\u001B[36m";
        String GREEN   = "\u001B[32m";
        String YELLOW  = "\u001B[33m";
        String BLUE    = "\u001B[34m";
        String BOLD    = "\u001B[1m";
        String RED     = "\u001B[31m";
        String GRAY    = "\u001B[90m";

        StringBuilder sb = new StringBuilder();

        // Header
        String orderType = isTableOrder() ? "Table " : "Online Order";
        sb.append(CYAN).append(BOLD)
        .append("\n")
        .append(" ORDER SUMMARY ").append(orderType).append("\n")
        .append("\n").append(RESET);

        // Items list
        if (foodItems.isEmpty()) {
            sb.append(GRAY).append("   No items in this order yet.\n").append(RESET);
        } else {
            sb.append(BLUE).append(" #   Item                          Qty    Price     Subtotal\n")
            .append("───────────────────────────────────────────────────────").append(RESET).append("\n");

            for (int i = 0; i < foodItems.size(); i++) {
                Food food = foodItems.get(i);
                double subtotal = food.getPrice() * quantities.get(i);

                String nameDisplay = food.getName();
                if (nameDisplay.length() > 25) {
                    nameDisplay = nameDisplay.substring(0, 22) + "...";
                }

                sb.append(String.format(GRAY + "%2d" + RESET + "  %-25s  " + GREEN + "%3d" + RESET + " x " 
                    + YELLOW + "$%5.2f" + RESET + "  =  " + BOLD + YELLOW + "$%6.2f" + RESET + "\n",
                    i + 1,
                    nameDisplay,
                    quantities.get(i),
                    food.getPrice(),
                    subtotal
                ));
            }
        }

        // Summary line
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(BOLD).append(" Total items: ").append(RESET).append(foodItems.size())
        .append("          ").append(BOLD).append("Grand Total: ").append(RESET)
        .append(GREEN).append(BOLD).append(String.format("$%.2f", totalAmount)).append(RESET).append("\n");

        // Status & Date
        String statusColor = switch (status.toLowerCase()) {
            case "completed", "paid"   -> GREEN;
            case "pending", "new"      -> YELLOW;
            case "cancelled", "failed" -> RED;
            default                    -> BLUE;
        };

        sb.append(GRAY).append(" Status: ").append(statusColor).append(BOLD).append(status).append(RESET)
        .append(GRAY).append("   -   Date: ").append(RESET).append(getFormattedDate()).append("\n");

        return sb.toString();
    }
    }