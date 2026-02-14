package models.inventory;
import models.Transaction;
import java.util.ArrayList;

public class TransactionInventory {
    private ArrayList<Transaction> TransactionList;
    public TransactionInventory(){
        this.TransactionList = new ArrayList<>();
    }
    
    public void addTransaction(Transaction transaction){
        if(transaction == null){
            throw new IllegalArgumentException("Not valid transaction!");
        }
        TransactionList.add(transaction);
    }

    public void displayInventory(){
        for (Transaction transaction : TransactionList) {
            System.out.println(transaction.getAmount() + transaction.getPaymentMethod() + transaction.getFormattedDate());
        }
    }
}
