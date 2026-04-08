package models.payment;

/** Always succeeds – the cashier physically receives the money. */
public class CashPayment extends BasePayment {
    public CashPayment() { super("Cash"); }

    @Override
    public boolean pay(double amount) {
        validateAmount(amount);
        System.out.println("  [Cash] Payment of $" + String.format("%.2f", amount) + " accepted.");
        return true;
    }
}
