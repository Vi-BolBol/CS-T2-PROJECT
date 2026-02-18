package controllers;
import java.util.ArrayList;

import models.Food;
import models.Food_quantity;
import models.Order;
import models.Transaction;
import models.Table;


import models.inventory.TransactionInventory;
import models.inventory.CartInventory;
import models.inventory.FoodInventory;
import models.inventory.TableInventory;



public class Controller{
    private FoodInventory inventory;
    private TableInventory tableInventory;

    private TransactionInventory transactionInventory;
    private CartInventory cart;

    public Controller(String id){
        inventory = new FoodInventory();
        transactionInventory = new TransactionInventory();
        cart = new CartInventory();
        tableInventory = new TableInventory();
    }


    public void addFood(String name, double price, String category, boolean available,int initialStock){
        Food food = new Food(name, price, category, available);
        inventory.addFood(food, initialStock);
    }

    public void addTable(int capacity, boolean isOccupied, boolean isVIP, String location, double basePrice){
        try{
            tableInventory.addTable(new Table(capacity, isOccupied, isVIP, location, basePrice));
        }
        catch(Error error){
            System.out.println(error);
        }
    }

    public boolean isIDExist(String id){
        return inventory.getFoodByIndex(id) != null;
    }

    public boolean validateTableIndex(String str){
        if(! str.matches("\\d+") || !tableInventory.isValidIndex(Integer.parseInt(str))){
            return false;
        }
        Table table = tableInventory.getTable(Integer.parseInt(str));

        if(table.isOccupied()){
            return false;
        }

        return true;
    }

    public boolean isFoodAvailable(String id){
        Food food = inventory.getFoodByIndex(id);
        return food.isAvailable();
    }

    // add food order to cart 
    public void addToCart(String index, int quantity){

        Food food = inventory.getFoodByIndex(index);

        if(food == null){
            System.out.println("food not found!");
            return;
        }

        Food_quantity food_quantity = new Food_quantity(food.getFoodId(),food.getName(), food.getPrice(), food.getCategory(), food.isAvailable(), quantity);

        try{
            inventory.reduceStockLevelByIndex(index, quantity);
            cart.addFood(food_quantity);

        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid amount!");
        }

    }

    // display food ordered in cart
    public void displayOrderedCart(){
        cart.printOrderList();
    }

    public int getCartSize(){
        return cart.getCartSize();
    }

    //create one order, one order may contain many food, with diferent quantity 
    public void order(String orderId, String orderType, String payment,String tableNumber){

        Order order = null;
        Table table = null;

        // Create Order for Table Service
        if(tableNumber != null){
            table = tableInventory.getTable(Integer.parseInt(tableNumber));
            table.setOccupied(true);

            order = new Order(orderId, orderType,table.getTableId());
        }

        // Create Order for Online Service 
        else{
            order = new Order(orderId, orderType);
        }

        // Transfer food from cart to Order 
        ArrayList<Food_quantity> allOrder = cart.getOrder();
        
        for(int i = 0;i < allOrder.size();i++){
            Food_quantity food = allOrder.get(i);
            order.addOrder(food.getFoodId(),food.getQuantity(),food.getPrice());
        }

        if(table != null){
            
        }

        // Calculate payment
        double totalPrice = order.getTotalPrice();

        // Print table info and add price to base price
        if(table != null){

            double tablePrice = table.getBasePrice();
            totalPrice += tablePrice;

            String vipStatus = table.isVIP() ? "[*VIP] " : "Normal";
            String priceDisplay = (tablePrice == 0) ? "Free" : String.format("$%.2f", tablePrice);

            System.out.println("┌────────────────── Table Details ──────────────────┐");
            System.out.printf("  %-10s : %-10s | %-10s : %-10s  \n", 
                            "Status", vipStatus, "Location", table.getLocation());
            System.out.printf("  %-10s : %-10s | %-10s : %-10d  \n", 
                            "Price", priceDisplay, "Capacity", table.getCapacity());
            System.out.println("└───────────────────────────────────────────────────┘");
        }

        this.paymentIntegration(order.getOrderId(), totalPrice, payment, "completed");

        cart.clearCartItem();
    }

    public void paymentIntegration(String orderId, double amount, String paymentMethod,String status){
        //create transaction
        //add to inventory for later see
        //print recipt out
        
        Transaction transaction = new Transaction(orderId, amount, paymentMethod, status,20);
        transactionInventory.addTransaction(transaction);
        System.out.println(transaction.getTransactionReceipt());
    }

    public void cancelOrder(){
        ArrayList<Food_quantity> allOrder = cart.getOrder();
        
        for(int i = 0;i < allOrder.size();i++){
            Food_quantity food = allOrder.get(i);
            System.out.println("restock" + food.getName() + "quantity" + food.getQuantity());
            inventory.AddtockLevelByID(food.getFoodId(), food.getQuantity());
        }

        cart.clearCartItem();
    }
    
    public void removeFoodFromInventoryByIndex(String index){
        inventory.removeFoodByIndex(index);
    }

    public void displayInventory(){
        inventory.displayInventory();
    }

    public void displayTableInventory(){
        tableInventory.displayInventory();
    }
}
