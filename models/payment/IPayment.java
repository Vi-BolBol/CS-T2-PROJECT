package models.payment;

public interface IPayment {   
    boolean pay(double amount);
    String getPaymentType();
}
