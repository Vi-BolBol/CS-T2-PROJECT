package models.payment;

/**
 * ABSTRACTION: defines the contract every payment method must fulfil.
 * POLYMORPHISM: the controller calls pay() without knowing the concrete type.
 */
public interface IPayment {

    /**
     * Attempt to charge {@code amount}.
     * @return true if payment succeeded, false otherwise.
     */
    boolean pay(double amount);

    /** Human-readable label used in receipts (e.g. "Cash", "Card"). */
    String getPaymentType();
}
