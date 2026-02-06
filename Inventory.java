import java.util.ArrayList;
import java.util.List;

public class Inventory {
    String inventoryId;
    List<Food> foodItems;
    List<Integer> stockLevels; 

    public Inventory(String inventoryId) {
        this.inventoryId = inventoryId;
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
            if (item.foodId.equals(food.foodId)) {
                throw new IllegalArgumentException("Food with ID " + food.foodId + " already exists in inventory");
            }
        }
        
        foodItems.add(food);
        stockLevels.add(initialStock);

        if (initialStock == 0) {
            food.available = false;
        }
    }

    public void removeFood(String foodId) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        
        int index = -1;
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).foodId.equals(foodId)) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            foodItems.remove(index);
            stockLevels.remove(index);
        }
    }

    public Food findFood(String foodId) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        
        for (Food food : foodItems) {
            if (food.foodId.equals(foodId)) {
                return food;
            }
        }
         return null;
    }

    public int getStock(String foodId) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).foodId.equals(foodId)) {
                return stockLevels.get(i);
            }
        }
        return -1;
    }


    //set old stock level to new stock level 
    public void updateStock(String foodId, int newStock) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock level cannot be negative");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).foodId.equals(foodId)) {
                stockLevels.set(i, newStock);

                Food food = foodItems.get(i);
                if (newStock == 0) {
                    food.available = false;
                } else {
                    food.available = true;
                }
                return;
            }
        }
    }

    //add to stock
    public void restockItem(String foodId, int quantity) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).foodId.equals(foodId)) {
                int currentStock = stockLevels.get(i);
                int newStock = currentStock + quantity;
                stockLevels.set(i, newStock);

                Food food = foodItems.get(i);
                if (!food.available && newStock > 0) {
                    food.available = true;
                }
                return;
            }
        }
    }

    //reduce 
    public void reduceStock(String foodId, int quantity) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).foodId.equals(foodId)) {
                int currentStock = stockLevels.get(i);
                
                if (currentStock < quantity) {
                    throw new IllegalArgumentException("Insufficient stock. Available: " + currentStock + ", Requested: " + quantity);
                }
                
                int newStock = currentStock - quantity;
                stockLevels.set(i, newStock);

                Food food = foodItems.get(i);
                if (newStock == 0) {
                    food.available = false;
                }
                return;
            }
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

    public void displayInventory(){
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
                    food.foodId,
                    food.name,
                    food.category,
                    food.price,
                    stock,
                    status);
        }

        System.out.println("──────────────────────────────────────────────────────────────");
        System.out.printf("  Total items          : %d%n", getTotalItems());
        System.out.printf("  Low stock items (<10): %d%n", getLowStockCount(10));
        System.out.println();
    }
}