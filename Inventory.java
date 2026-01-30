import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private String inventoryId;
    private List<Food> foodItems;
    private List<Integer> stockLevels; 

    public Inventory(String inventoryId) {
        setInventoryId(inventoryId);
        this.foodItems = new ArrayList<>();
        this.stockLevels = new ArrayList<>();
    }

    // Getters
    public String getInventoryId() {
        return inventoryId;
    }

    public List<Food> getFoodItems() {
        return new ArrayList<>(foodItems); 
    }

    public List<Integer> getStockLevels() {
        return new ArrayList<>(stockLevels); 
    }

    public void setInventoryId(String inventoryId) {
        if (inventoryId == null || inventoryId.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory ID cannot be empty");
        }
        this.inventoryId = inventoryId;
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
            food.setAvailable(false);
        }
    }

    public void removeFood(String foodId) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        
        int index = -1;
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getFoodId().equals(foodId)) {
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
            if (food.getFoodId().equals(foodId)) {
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
            if (foodItems.get(i).getFoodId().equals(foodId)) {
                return stockLevels.get(i);
            }
        }
        return -1;
    }

    public void updateStock(String foodId, int newStock) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock level cannot be negative");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getFoodId().equals(foodId)) {
                stockLevels.set(i, newStock);

                Food food = foodItems.get(i);
                if (newStock == 0) {
                    food.setAvailable(false);
                } else {
                    food.setAvailable(true);
                }
                return;
            }
        }
    }

    public void restockItem(String foodId, int quantity) {
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

                Food food = foodItems.get(i);
                if (!food.isAvailable() && newStock > 0) {
                    food.setAvailable(true);
                }
                return;
            }
        }
    }

    public void reduceStock(String foodId, int quantity) {
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getFoodId().equals(foodId)) {
                int currentStock = stockLevels.get(i);
                
                if (currentStock < quantity) {
                    throw new IllegalArgumentException("Insufficient stock. Available: " + currentStock + ", Requested: " + quantity);
                }
                
                int newStock = currentStock - quantity;
                stockLevels.set(i, newStock);

                Food food = foodItems.get(i);
                if (newStock == 0) {
                    food.setAvailable(false);
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

    public void displayInventory() {
        System.out.println("===== INVENTORY REPORT =====");
        if (foodItems.isEmpty()) {
            System.out.println("No items in inventory");
        } else {
            for (int i = 0; i < foodItems.size(); i++) {
                Food food = foodItems.get(i);
                int stock = stockLevels.get(i);
                String stockStatus = stock == 0 ? "OUT OF STOCK" : 
                                   (stock < 10 ? "LOW STOCK" : "In Stock");
                System.out.println(String.format("%s | Stock: %d (%s)", 
                    food.getDisplayInfo(), stock, stockStatus));
            }
        }
        System.out.println("Total Items: " + getTotalItems());
        System.out.println("Low Stock Items (< 10): " + getLowStockCount(10));
    }
}