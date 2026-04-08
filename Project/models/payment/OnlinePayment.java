package models.payment;

/** Online / e-wallet payment – always succeeds in this simulation. */
public class OnlinePayment extends BasePayment {
    public OnlinePayment() { super("Online"); }

    @Override
    public boolean pay(double amount) {
        validateAmount(amount);
        System.out.printf("  [Online] E-wallet charged $%.2f.%n", amount);
        return true;
    }
}
