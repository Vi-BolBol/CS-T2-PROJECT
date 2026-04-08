package models.payment;

/**
 * ABSTRACTION + INHERITANCE: holds the payment-type label that every
 * concrete payment shares.  Subclasses only implement the pay() logic.
 *
 * ENCAPSULATION: paymentType is private and immutable after construction.
 */
public abstract class BasePayment implements IPayment {

    private final String paymentType;

    protected BasePayment(String paymentType) {
        if (paymentType == null || paymentType.isBlank())
            throw new IllegalArgumentException("Payment type cannot be empty.");
        this.paymentType = paymentType;
    }

    @Override
    public String getPaymentType() { return paymentType; }

    /**
     * Shared pre-condition: amount must be positive.
     * Concrete subclasses call this to validate before their own logic.
     */
    protected void validateAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Payment amount must be greater than 0.");
    }

    /** POLYMORPHISM: each subclass defines its own approval logic. */
    @Override
    public abstract boolean pay(double amount);
}
