package models.payment;

/** QR-code payment – minimum charge of $1. */
public class QRPayment extends BasePayment {
    public QRPayment() { super("QR"); }

    @Override
    public boolean pay(double amount) {
        validateAmount(amount);
        if (amount < 1.0) {
            System.out.println("  [QR] Minimum QR payment is $1.00.");
            return false;
        }
        System.out.printf("  [QR] Scan complete – $%.2f charged.%n", amount);
        return true;
    }
}
