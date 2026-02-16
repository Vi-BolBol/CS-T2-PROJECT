package models;
import java.util.UUID;;
public class Food {
    private String foodId;
    private String name;
    private double price;
    private String category;
    private boolean available;

    public Food(String name, double price, String category, boolean available) {
        this.foodId = UUID.randomUUID().toString();
        setName(name);
        setPrice(price);
        setCategory(category);
        setAvailable(available);
    }

    public String getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = price;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDisplayInfo() {
        return String.format("[%s] %s - $%.2f (%s) - %s", 
            foodId, name, price, category, available ? "Available" : "Sold Out");
    }
}