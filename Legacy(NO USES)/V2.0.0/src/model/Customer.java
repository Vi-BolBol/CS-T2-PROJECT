package model;

public class Customer {
    int CustomerID;
    String name;
    double balance;
    boolean member;

    public Customer(int CustomerID,String name, double balance, boolean member){
        this.CustomerID = CustomerID;
        this.name = name;
        this.balance = balance;
        this.member = member;
    }

    public boolean payment(double amount){
        if(amount <= 0) return false;
        if (balance >= amount){
            balance -= amount;
            return true;
        }
        return false;
    }
}
