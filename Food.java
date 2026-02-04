public class Food {
    private String foodId;
    private String name;
    private double price;
    private String category;
    private boolean available;

    public Food(String foodId, String name, double price, String category, boolean available) {
        setFoodId(foodId);
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

    public void setFoodId(String foodId) {
        //Validate , food must be not null and empty 
        if (foodId == null || foodId.trim().isEmpty()) {
            throw new IllegalArgumentException("Food ID cannot be empty");
        }
        this.foodId = foodId;
    }

    public void setName(String name) {
        //Validate , food must be not null and empty 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Food name cannot be empty");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        //Validate, price must be > 0
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = price;
    }

    public void setCategory(String category) {
        //Validate, Category must be not null and empty 
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDisplayInfo() {
        //print format
        //foodId, name, price, category, available ? "Available" : "Sold Out"
        String availability = (available) ? "Available":"Sold Out";
        return foodId + name + price + category + availability;
    }
}