package models;

import db.DatabaseObject;

/**
 * ENCAPSULATION: all fields private, access through validated setters.
 * INHERITANCE: extends DatabaseObject for id/timestamp management.
 */
public class Food extends DatabaseObject {

    private String name;
    private double price;
    private String category;
    private boolean available;
    private int    quantity;

    public Food(String name, double price, String category, int quantity) {
        super();
        setName(name);
        setPrice(price);
        setCategory(category);
        setQuantity(quantity);         // syncAvailable is called inside setQuantity
    }

    // ── Derived state ─────────────────────────────────────────────────────────

    /** Keeps 'available' consistent with 'quantity' – call after any qty change. */
    public void syncAvailable() {
        this.available = (quantity > 0);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String  getName()      { return name; }
    public double  getPrice()     { return price; }
    public String  getCategory()  { return category; }
    public boolean isAvailable()  { return available; }
    public int     getQuantity()  { return quantity; }

    // ── Setters (validated) ───────────────────────────────────────────────────

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Food name cannot be empty.");
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (price <= 0 || price > 10_000)
            throw new IllegalArgumentException("Price must be between 0 and 10,000.");
        this.price = price;
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank())
            throw new IllegalArgumentException("Category cannot be empty.");
        this.category = category.trim();
    }

    public void setQuantity(int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
        syncAvailable();                // always keep available in sync
    }

    // ── DatabaseObject contract ───────────────────────────────────────────────

    @Override
    public String getSummary() {
        return String.format("Food[%s | $%.2f | %s | qty:%d | %s]",
                name, price, category, quantity, available ? "Available" : "Sold Out");
    }
}
