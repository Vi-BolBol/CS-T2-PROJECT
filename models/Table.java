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

    public void TableInfo(){
        String RESET  = "\u001B[0m";
        String CYAN   = "\u001B[36m";
        String GREEN  = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String RED    = "\u001B[31m";
        String GRAY   = "\u001B[90m";
        String BOLD   = "\u001B[1m";

        System.out.println(CYAN + "                   Your Ordered Table" + RESET);

        String statusLine = switch (this.getStatus().toLowerCase()) {
            case "available", "free"   -> GREEN + " Available" + RESET;
            case "reserved", "booked"  -> YELLOW + " Reserved" + RESET;
            case "occupied", "in-use"  -> RED + " Occupied" + RESET;
            default                    -> GRAY + this.getStatus() + RESET;
        };

        System.out.printf(BOLD + " %d seats   " + YELLOW + "$%.2f" + RESET + "   %s\n",
                this.getCapacity(), this.getPrice(), statusLine);

        System.out.println("   " + GRAY + this.getLocation() + RESET + " > " + this.getDescription());
        System.out.println();

        System.out.println(CYAN + "══════════════════════════════════════════════════════════════" + RESET);
        }
}