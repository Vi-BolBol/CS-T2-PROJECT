package models.inventory;
import java.util.ArrayList;
import java.util.List;
//import java.util.UUID;

import models.Food;

public class FoodInventory {
    //private String inventoryId;
    private List<Food> foodItems;
    private List<Integer> stockLevels; 

    public FoodInventory() {
        //this.inventoryId = UUID.randomUUID().toString();
        this.foodItems = new ArrayList<>();
        this.stockLevels = new ArrayList<>();
    }
 
    public void addFood(Food food, int initialStock) {
        if (food == null) {
            throw new IllegalArgumentException("Food item cannot be null");
        }
        if (initialStock < 0) {
            throw new IllegalArgumentException("Initial stock cannot be negative");
        }

        for (Food item : foodItems) {
            if (item.getFoodId().equals(food.getFoodId())) {
                throw new IllegalArgumentException("Food with ID " + food.getFoodId() + " already exists in inventory");
            }
        }
        
        foodItems.add(food);
        stockLevels.add(initialStock);

        if (initialStock == 0) {
            food.setAvailable(true); 
        }
    }

    public Food getFoodByIndex(String index){

        if(!index.matches("\\d+")){
            return null;
        }

        int i = Integer.parseInt(index) -1;

        if( i >= foodItems.size() || i < 0){
            return null;
        }

        return foodItems.get(i);
    }

    public void removeFoodByIndex(String index){

        if (index == null || index.trim().isEmpty() ) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }

        if(!index.matches("\\d+")){
            throw new IllegalArgumentException("Index must be Number!");
        }

        int i = Integer.parseInt(index) -1;

        if(i >= foodItems.size() || i < 0){
            throw new IllegalArgumentException("Index out of bound");
        }

        foodItems.remove(i);
        stockLevels.remove(i);

    }

    public void addStockLevelByIndex(String index, int quantity){
        Food food = this.getFoodByIndex(index);
        if(food == null){
            throw new IllegalArgumentException("Invalid index");
        }

        int i = Integer.parseInt(index) - 1;

        stockLevels.set(i, quantity + stockLevels.get(i));

        if (!food.isAvailable() && quantity > 0) {
            food.setAvailable(true); 
        }

    }

    public void reduceStockLevelByIndex(String index, int quantity){
        Food food = this.getFoodByIndex(index);

        if(food == null){
            throw new IllegalArgumentException("Invalid index");
        }

        int i = Integer.parseInt(index) - 1;

        int currentStock = stockLevels.get(i);

        if (currentStock < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + currentStock + ", Requested: " + quantity);
        }

        stockLevels.set(i,currentStock - quantity);

        if (stockLevels.get(i) == 0) {
            food.setAvailable(false); 
        }

    }

    public int getStockByIndex(String index){

        if(!index.matches("\\d+")){
            throw new IllegalArgumentException("Index must be Number!");
        }

        int i = Integer.parseInt(index) -1;

        if(i >= foodItems.size() || i < 0){
            throw new IllegalArgumentException("Index out of bound");
        }

        return stockLevels.get(i);
    }

    public void setStockLevelByIndex(String index, int quantity){
        Food food = this.getFoodByIndex(index);
        if(food == null){
            throw new IllegalArgumentException("Invalid index");
        }

        int i = Integer.parseInt(index) - 1;

        stockLevels.set(i, quantity);

        if (quantity == 0) {
            food.setAvailable(false); 
        } else {
            food.setAvailable(true); 
        }
    }

    public int getTotalItems() {
        return foodItems.size();
    }

    public int getLowStockCount(int threshold) {
        int count = 0;
        for (int stock : stockLevels) {
            if (stock < threshold) {
                count++;
            }
        }
        return count;
    }

    public void AddtockLevelByID(String foodId, int quantity) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getFoodId().equals(foodId)) {
                int currentStock = stockLevels.get(i);
                int newStock = currentStock + quantity;
                stockLevels.set(i, newStock);

                System.out.println("restock completed");
                Food food = foodItems.get(i);
                if (!food.isAvailable() && newStock > 0) {
                    food.setAvailable(true);
                }
                return;
            }
            System.out.println(foodItems.get(i).getFoodId().equals(foodId));
        }
    }

    public void displayInventory(){
        System.out.println("");
        System.out.println("");
        System.out.println("═══════════════════════ INVENTORY REPORT ═══════════════════════");
        System.out.println();

        if (foodItems.isEmpty()) {
            System.out.println("                  No items in inventory");
            System.out.println();
            return;
        }

        System.out.println(" ID   Name                 Category     Price    Stock   Status");
        System.out.println("──────────────────────────────────────────────────────────────");

        for (int i = 0; i < foodItems.size(); i++) {
            Food food = foodItems.get(i);
            int stock = stockLevels.get(i);

            String status = stock == 0 ? "OUT OF STOCK" :
                            stock < 10 ? "LOW STOCK" : "In Stock";

            System.out.printf("%3s  %-20s %-12s $%6.2f   %4d   %s%n",
                    i+1,
                    food.getName(),
                    food.getCategory(),
                    food.getPrice(),
                    stock,
                    status);
        }

        System.out.println("──────────────────────────────────────────────────────────────");
        System.out.printf("  Total items          : %d%n", getTotalItems());
        System.out.printf("  Low stock items (<10): %d%n", getLowStockCount(10));
        System.out.println();
    }
}