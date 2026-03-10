package models.payment;

public class QRPayment extends BasePayment {

    public QRPayment() {
        super("QR");
    }

    @Override
    public boolean pay(double amount) {
        return amount >= 1;
    }
}