package models.payment;

public abstract class BasePayment implements IPayment{
    private String paymentType;

    public BasePayment(String paymentType){
        if (paymentType == null || paymentType.trim().isEmpty()){
            throw new IllegalArgumentException("Payment cannot be empty");
        }
        this.paymentType = paymentType;
    }

    @Override
    public String getPaymentType() {
        return paymentType;
    }

    @Override
    public abstract boolean pay(double amount);

}
