package models.payment;

public class CardPayment extends BasePayment{
    private String cardNumber;

    public CardPayment(String cardNumber){
        super("Card");
        if (cardNumber == null || cardNumber.trim().isEmpty()){
            throw new IllegalArgumentException();
        }
        this.cardNumber = cardNumber;
    }

    public String getcardNumber(){
        return cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        return amount <= 5000;
    }

    
}
