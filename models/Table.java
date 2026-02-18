package models;
import java.util.UUID;

public class Table {

    private String tableId;
    private int capacity;
    private boolean isOccupied;
    private String location;
    private boolean isVIP;
    private double basePrice;

    public Table(int capacity, boolean isOccupied, boolean isVIP, String location, double basePrice) {
        // Use setters in constructor to ensure validation logic is applied
        this.tableId = UUID.randomUUID().toString();
        setCapacity(capacity);
        setOccupied(isOccupied);
        setVIP(isVIP);
        setLocation(location);
        setBasePrice(basePrice);
    }

    // --- Setters with Validation ---

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.capacity = capacity;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }
        this.location = location;
    }

    public void setVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }
    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.basePrice = basePrice;
    }

    // --- Getters ---

    public String getTableId() { return tableId; }
    public int getCapacity() { return capacity; }
    public boolean isOccupied() { return isOccupied; }
    public String getLocation() { return location; }
    public boolean isVIP() { return isVIP; }
    public double getBasePrice() {return basePrice; };

    public String getTableInfo() {
        String status = isOccupied ? "Occupied" : "Available";
        String vipStatus = isVIP ? "[VIP] " : "";
        return String.format("%sTable (%s) - Capacity: %d - %s", 
            vipStatus, location, capacity, status);
    }
}