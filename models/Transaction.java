package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import db.DatabaseObject;

public class Transaction extends DatabaseObject{

    private String orderId;

    private double foodAmount;
    private double tableAmount;
    private double discount;
    private String paymentMethod;
    private LocalDateTime transactionDate;
    private String status;

    public Transaction(String orderId, double foodAmount,double tableAmount,double discount, String paymentMethod) {
        setOrderId(orderId);

        setFoodAmount(foodAmount);
        setTableAmount(tableAmount);
        setDiscount(discount);

        setPaymentMethod(paymentMethod);
        this.transactionDate = LocalDateTime.now();
        setStatus("Completed");
    }


    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return foodAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public double discountCalculator(){
        double total = foodAmount + tableAmount;
        return total - (total * discount) / 100;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId;
    }

    public void setFoodAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.foodAmount = amount;
    }
    public void setTableAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Table charge must be positive");
        }
        this.tableAmount = amount;
    }

    public void setDiscount(double percent) {
        if (percent < 0) {
            throw new IllegalArgumentException("Table charge must be positive");
        }
        this.discount = percent;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }
        String upperMethod = paymentMethod.toUpperCase();
        if (!upperMethod.equals("CASH") && !upperMethod.equals("CARD") && 
            !upperMethod.equals("QR") && !upperMethod.equals("ONLINE")) {
            throw new IllegalArgumentException("Invalid payment method. Must be: Cash, Card, QR, or Online");
        }
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("PENDING") && !upperStatus.equals("COMPLETED") && 
            !upperStatus.equals("FAILED") && !upperStatus.equals("REFUNDED")) {
            throw new IllegalArgumentException("Invalid status. Must be: Pending, Completed, Failed, or Refunded");
        }
        this.status = status;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return transactionDate.format(formatter);
    }

    public boolean isSuccessful() {
        return status.equalsIgnoreCase("Completed");
    }

    public void transactionInfo() {
        String RESET  = "\u001B[0m";
        String CYAN   = "\u001B[36m";
        String GREEN  = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String BLUE   = "\u001B[34m";
        String BOLD   = "\u001B[1m";
        String GRAY   = "\u001B[90m";
        String RED    = "\u001B[31m";

        // Header
        System.out.println(CYAN + BOLD + "═══════════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + BOLD + "                  TRANSACTION SUMMARY" + RESET);
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════════" + RESET);

        // ──────────────────────────────── Items ────────────────────────────────
        System.out.printf(BLUE + " %-22s" + RESET + " : " + YELLOW + "$%8.2f" + RESET + "%n",
                "Food Amount", foodAmount);

        System.out.printf(BLUE + " %-22s" + RESET + " : " + YELLOW + "$%8.2f" + RESET + "%n",
                "Table Amount", tableAmount);   // ← assuming you have tableAmount (was duplicate foodAmount)

        double subtotal = foodAmount + tableAmount;   // corrected: was using foodAmount twice

        System.out.println(GRAY + "───────────────────────────────────────────────────────────────" + RESET);

        System.out.printf(BOLD + " %-22s" + RESET + " : " + GREEN + BOLD + "$%8.2f" + RESET + "%n",
                "Sub-total", subtotal);

        System.out.printf(" %-22s" + RESET + " : " + RED + "-$%7.2f" + RESET + "%n",
                "Discount", (subtotal * discount) /100);

        double finalTotal = this.discountCalculator();   // assuming this returns price after discount

        System.out.println(GRAY + "───────────────────────────────────────────────────────────────" + RESET);

        System.out.printf(BOLD + " %-22s" + RESET + " : " + GREEN + BOLD + "$%8.2f" + RESET + "%n",
                "TOTAL TO PAY", finalTotal);

        System.out.println(GRAY + "───────────────────────────────────────────────────────────────" + RESET);

        // Payment & Status
        String statusColor = switch (status.toLowerCase()) {
            case "completed", "paid", "success" -> GREEN;
            case "pending", "processing"        -> YELLOW;
            case "failed", "cancelled"          -> RED;
            default                             -> GRAY;
        };

        System.out.printf(BLUE + " %-22s" + RESET + " : %s%s%s%n",
                "Payment Method", BOLD, paymentMethod, RESET);

        System.out.printf(BLUE + " %-22s" + RESET + " : %s%n",
                "Date", this.getFormattedDate());

        System.out.printf(BLUE + " %-22s" + RESET + " : " + statusColor + BOLD + "%s" + RESET + "%n",
                "Status", status);

        System.out.println(CYAN + "═══════════════════════════════════════════════════════════════" + RESET);
    }

}
