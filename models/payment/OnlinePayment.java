package models.payment;

public class OnlinePayment extends BasePayment {

    public OnlinePayment() {
        super("Online");
    }

    @Override
    public boolean pay(double amount) {
        return true;
    }
}