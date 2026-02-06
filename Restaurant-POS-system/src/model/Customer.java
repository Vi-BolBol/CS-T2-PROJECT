package model;

public class Customer {
    String name;
    double balance;
    boolean member;

    public Customer(String name, double balance, boolean member){
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

    @Override
    public String toString(){
        return "Customer name: '" + name + "', balance = " + balance + ", member = " + member;
    }
}
