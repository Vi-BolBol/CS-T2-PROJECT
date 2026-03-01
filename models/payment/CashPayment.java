package models.payment;

public class CashPayment extends BasePayment{
    public CashPayment(){
        super("Cash");
    }

    @Override
    public boolean pay(double amount){
        return true;
    }
}
