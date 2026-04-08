package model;

public class Order {
    int OrderID;
    Customer customer;
    Food[] items;
    int itemCounter;

    Order(int OrderID, Customer customer){
        this.OrderID = OrderID;
        this.customer = customer;
        this.items = new Food[2];
        this.itemCounter = 0;
    }

    void expandItems(){
        int newCap = items.length * 2;
        Food[] newArr = new Food[newCap];

        for(int i = 0; i < itemCounter; i++) newArr[i] = items[i];

        items = newArr;
    }

    void addNewItem(Food item){
        if(itemCounter >= items.length) expandItems();
        items[itemCounter] = item;
        itemCounter++;
    }

    double calculateTotal(){
        double total = 0.0;
        for(int i = 0; i < itemCounter; i++) total += items[i].price;
        return total;
    }
}
