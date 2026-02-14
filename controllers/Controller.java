package controllers;
import java.util.ArrayList;

import models.Food;
import models.Food_quantity;
import models.inventory.TransactionInventory;
import models.inventory.CartInventory;
import models.inventory.FoodInventory;
import models.Order;
import models.Transaction;

public class Controller{
    private FoodInventory inventory = null;
    private TransactionInventory transactionInventory = null;
    private CartInventory cart = null;

    public Controller(String id){
        inventory = new FoodInventory();
        transactionInventory = new TransactionInventory();
        cart = new CartInventory();
    }

    // add food to stock for selling purpose
    public void addFood(String foodId, String name, double price, String category, boolean available,int initialStock){
        Food food = new Food(foodId, name, price, category, available);
        inventory.addFood(food, initialStock);
    }

    public boolean isIDExist(String id){
        return inventory.findFood(id) != null;
    }

    // add to cart 
    public void addToCart(String id, int quantity){
        Food food = inventory.findFood(id);

        //Food food = inventory.getFoodByIndex(Integer.parseInt(id) -1);

        if(food == null){
            System.out.println("food not found!");
            return;
        }

        Food_quantity food_quantity = new Food_quantity(food.getFoodId(), food.getName(), food.getPrice(), food.getCategory(), food.isAvailable(), quantity);

        try{
            inventory.reduceStockLevel(id, quantity);
            cart.addFood(food_quantity);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid amount!");
        }

    }

    public void cancelOrder(String[] ids,int[] quantities){
        for(int i = 0;i<ids.length;i++){
            inventory.addStockLevel(ids[i],quantities[i]);
        }
        cart.clearCartItem();
    }

    // display food ordered in cart
    public void displayOrderedCart(){
        cart.printOrderList();
    }

    public int getCartSize(){
        return cart.getCartSize();
    }

    // transaction history
    public void transactionHistory(){
        transactionInventory.displayInventory();
    }

    //create one order, one order may contain many food, with diferent quantity 
    public void order(String orderId, String orderType , String[] foodId, int[] quantities){
        Order order = new Order(orderId, orderType);

        if(foodId.length != quantities.length){
            System.out.println("Food Id length must be equal to Quantity lenth");
            return;
        }

        for( int i = 0 ; i < foodId.length ; i++){
            Food food = inventory.findFood(foodId[i]);
            //Food food = inventory.getFoodByIndex(Integer.parseInt(foodId[i]) -1);
            

            if(food == null){
                System.out.println("Food ID " + foodId[i] + " Not found !");
                return;
            }
            else{
                order.addOrder(food.getFoodId(), quantities[i], food.getPrice());
            }
        }

        System.out.println(order.getOrderSummary());

        Transaction transaction = new Transaction(orderId, orderId, order.getTotalPrice() ,"QR" ,"completed");
        transactionInventory.addTransaction(transaction);
        System.out.println(transaction.getTransactionReceipt());

    }

    public void displayInventory(){
        inventory.displayInventory();
    }
}
