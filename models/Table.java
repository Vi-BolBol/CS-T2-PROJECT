package models;

import db.DatabaseObject;

public class Table extends DatabaseObject {
    private int capacity;
    private boolean isOccupied;
    private String location;
    private String description;
    private double price;

    public Table(int capacity, boolean isOccupied, String location,double price,String des) {
        setCapacity(capacity);
        setOccupied(isOccupied);
        setLocation(location);
        setPrice(price);
        setDescription(des);
    }

    public int getCapacity() {return capacity;}
    public boolean isOccupied() {return isOccupied;}
    public String getLocation() {return location;}
    public String getDescription(){return this.description;}
    public double getPrice(){return this.price; }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
    }

    public void setPrice(double price) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.price = price;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location;
    }

    public void setDescription(String des) {
        if (des == null || des.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.description = des;
    }

    public String getStatus() {
        return isOccupied ? "Occupied" : "Available";
    }
}