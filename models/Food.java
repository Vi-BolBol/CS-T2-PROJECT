package models;
import db.DatabaseObject;

public class Food extends DatabaseObject{
    private String name;
    private double price;
    private String category;
    private boolean available;
    private int quantity;

    public Food(String name, double price, String category, boolean available,int quantity) {
        super();
        setName(name);
        setPrice(price);
        setCategory(category);
        setAvailable(available);
        setQuantity(quantity);
        syncAvilable();
    }

    public void syncAvilable(){
        if(quantity == 0){
            available = false;
        }
        else{
            available = true;
        }
    }

    public int getQuantity(){
        return quantity;
    }
    
    public void setQuantity(int quantity){
        if(quantity < 0){
            throw new IllegalArgumentException("Quantity must be possitive");
        }
        this.quantity = quantity;
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
        return String.format("%s - $%.2f (%s) - %s %d", 
            name, price, category, available ? "Available" : "Sold Out",quantity);
    }
}