package models.payment;

public class CardPayment extends BasePayment {

    private String cardNumber;

    public CardPayment(String cardNumber) {
        super("Card");
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be empty");
        }
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        return amount <= 5000;
    }
}