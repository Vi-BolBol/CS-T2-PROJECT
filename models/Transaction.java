package models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
public class Transaction {

    private String transactionId;
    private String orderId;
    private double amount;
    private String paymentMethod;
    private LocalDateTime transactionDate;
    private String status;

    public Transaction(String orderId, double amount, String paymentMethod,String status) {
        this.transactionId = UUID.randomUUID().toString();
        setOrderId(orderId);
        setAmount(amount);
        setPaymentMethod(paymentMethod);
        this.transactionDate = LocalDateTime.now();
        setStatus(status);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
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

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
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

    public String getTransactionReceipt() {
        System.out.println("");
      return String.format("══════════ RECEIPT ══════════\n" +
                           //"Transaction ID: %s\n" +
                           //"Order ID: %s\n" +
                           "Amount: $%.2f\n" +
                           "Payment Method: %s\n" +
                           "Status: %s\n" +
                           "Date: %s\n" +
                           "═════════ SUNFLOWER ═════════",
                            amount, paymentMethod, status, getFormattedDate());
    }
}