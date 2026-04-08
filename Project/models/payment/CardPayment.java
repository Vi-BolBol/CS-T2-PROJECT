package models.payment;

/**
 * ENCAPSULATION: card number is private; masked on display.
 * INHERITANCE: shares BasePayment's validateAmount() and getPaymentType().
 */
public class CardPayment extends BasePayment {

    private final String cardNumber;
    private static final double MAX_CARD_AMOUNT = 5_000.0;

    public CardPayment(String cardNumber) {
        super("Card");
        if (cardNumber == null || cardNumber.isBlank())
            throw new IllegalArgumentException("Card number cannot be empty.");
        this.cardNumber = cardNumber;
    }

    /** Returns the last-4 digits only (data hiding). */
    public String getMaskedCardNumber() {
        String n = cardNumber.replaceAll("\\s+", "");
        return "**** **** **** " + (n.length() >= 4 ? n.substring(n.length() - 4) : n);
    }

    @Override
    public boolean pay(double amount) {
        validateAmount(amount);
        if (amount > MAX_CARD_AMOUNT) {
            System.out.printf("  [Card] Declined – amount $%.2f exceeds card limit $%.2f.%n",
                    amount, MAX_CARD_AMOUNT);
            return false;
        }
        System.out.printf("  [Card] %s charged $%.2f.%n", getMaskedCardNumber(), amount);
        return true;
    }
}
