package models.payment;

public abstract class BasePayment implements IPayment {

    private String paymentType;

    public BasePayment(String paymentType) {
        if (paymentType == null || paymentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment type cannot be empty");
        }
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public abstract boolean pay(double amount);
}