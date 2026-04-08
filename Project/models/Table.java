package models;

import db.DatabaseObject;

/**
 * ENCAPSULATION: all fields private with validated setters.
 * INHERITANCE: extends DatabaseObject.
 */
public class Table extends DatabaseObject {

    private int    capacity;
    private boolean occupied;
    private String location;
    private String description;
    private double price;

    public Table(int capacity, boolean occupied, String location, double price, String description) {
        super();
        setCapacity(capacity);
        setOccupied(occupied);
        setLocation(location);
        setPrice(price);
        setDescription(description);
    }

    // ── Derived state ─────────────────────────────────────────────────────────

    public String getStatus() { return occupied ? "Occupied" : "Available"; }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int     getCapacity()    { return capacity; }
    public boolean isOccupied()     { return occupied; }
    public String  getLocation()    { return location; }
    public String  getDescription() { return description; }
    public double  getPrice()       { return price; }

    // ── Setters (validated) ───────────────────────────────────────────────────

    public void setCapacity(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than 0.");
        this.capacity = capacity;
    }

    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public void setLocation(String location) {
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be empty.");
        this.location = location.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be empty.");
        this.description = description.trim();
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Table price cannot be negative.");
        this.price = price;
    }

    // ── DatabaseObject contract ───────────────────────────────────────────────

    @Override
    public String getSummary() {
        return String.format("Table[cap:%d | %s | $%.2f | %s | %s]",
                capacity, location, price, getStatus(), description);
    }

    public void printTableInfo() {
        final String RESET  = "\u001B[0m", CYAN = "\u001B[36m", GREEN = "\u001B[32m",
                     YELLOW = "\u001B[33m", RED  = "\u001B[31m", GRAY  = "\u001B[90m",
                     BOLD   = "\u001B[1m";

        System.out.println(CYAN + "  Your Selected Table" + RESET);

        String statusDisplay = switch (getStatus().toLowerCase()) {
            case "available" -> GREEN  + " Available" + RESET;
            case "occupied"  -> RED    + " Occupied"  + RESET;
            default          -> YELLOW + " Reserved"  + RESET;
        };

        System.out.printf(BOLD + "  %d seats   " + YELLOW + "$%.2f" + RESET + "   %s%n",
                capacity, price, statusDisplay);
        System.out.println("  " + GRAY + location + RESET + " – " + description);
        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);
    }
}
