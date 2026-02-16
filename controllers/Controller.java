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
    public void addFood(String name, double price, String category, boolean available,int initialStock){
        Food food = new Food(name, price, category, available);
        inventory.addFood(food, initialStock);
    }

    public boolean isIDExist(String id){
        return inventory.getFoodByIndex(id) != null;
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

        Food_quantity food_quantity = new Food_quantity(food.getName(), food.getPrice(), food.getCategory(), food.isAvailable(), quantity);

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
    public void order(String orderId, String orderType , String[] foodId, int[] quantities, String payment){
        Order order = new Order(orderId, orderType);
        if(foodId.length != quantities.length){
            System.out.println("Food Id length must be equal to Quantity lenth");
            return;
        }

        ArrayList<Food_quantity> allOrder = cart.getOrder();
        
        for(int i = 0;i < allOrder.size();i++){
            Food_quantity food = allOrder.get(i);
            order.addOrder(food.getFoodId(),food.getQuantity(),food.getPrice());
            System.out.println("cart size is" + food.getPrice());
        }


        // for( int i = 0 ; i < foodId.length ; i++){

        //     Food food = inventory.getFoodByIndex(foodId[i]);
            
        //     if(food == null){
        //         System.out.println("Food ID " + foodId[i] + " Not found !");
        //         return;
        //     }
        //     else{
        //         order.addOrder(food.getFoodId(), quantities[i], food.getPrice());
        //     }
        // }

        System.out.println("order price is " + order.getTotalPrice());
        this.paymentIntegration(order.getOrderId(), order.getTotalPrice(), payment, "completed");
        cart.clearCartItem();
    }

    public void paymentIntegration(String orderId, double amount, String paymentMethod,String status){
        //create transaction
        //add to inventory for later see
        //print recipt out
        
        Transaction transaction = new Transaction(orderId, amount, paymentMethod, status);
        transactionInventory.addTransaction(transaction);
        System.out.println(transaction.getTransactionReceipt());
    }

    public void cancelOrder(String[] ids,int[] quantities){
        for(int i = 0;i<ids.length;i++){
            //  get id from food in inventory 
            inventory.addStockLevelByIndex(ids[i], quantities[i]);
        }
        cart.clearCartItem();
    }
    
    public void removeFoodFromInventoryByIndex(String index){
        inventory.removeFoodByIndex(index);
    }

    public void displayInventory(){
        inventory.displayInventory();
    }

}
