package models.inventory;

import models.Table;
import java.util.ArrayList;

public class TableInventory {
    private ArrayList<Table> tables;

    public TableInventory() {
        this.tables = new ArrayList<>();
    }

    // --- CREATE ---
    public void addTable(Table table) {
        if (table != null) {
            tables.add(table);
        }
    }

    // --- READ ---
    public Table getTable(int index) {
        index --;
        if (isValidIndex(index + 1)) {
            return tables.get(index);
        }
        return null; // Or throw an exception depending on your error handling style
    }

    public ArrayList<Table> getAllTables() {
        return new ArrayList<>(tables); // Returns a copy to protect the original list
    }

    // --- UPDATE ---
    public void updateTable(int index, Table newTable) {
        index --;
        if (isValidIndex(index) && newTable != null) {
            tables.set(index, newTable);
        } else {
            System.out.println("Invalid index or null table provided.");
        }
    }

    // --- DELETE ---
    public void removeTable(int index) {
        index --;
        if (isValidIndex(index)) {
            tables.remove(index);
        }
    }

    // Helper method to validate index bounds
    public boolean isValidIndex(int index) {
        index --;
        return index  >= 0 && index < tables.size();
    }

    public int getSize() {
        return tables.size();
    }
    
    public void displayInventory() {
        if (tables.isEmpty()) {
            System.out.println("\n[!] Inventory is currently empty.");
            return;
        }

        // Expanded table to fit the Price column
        String separator = "+-------+----------+----------------+------------+----------+-----------+";
        String header    = "| Index | Capacity | Location       | Occupied   | VIP      | Price     |";
        
        System.out.println("\n" + separator);
        System.out.println(header);
        System.out.println(separator);

        for (int i = 0; i < tables.size(); i++) {
            Table t = tables.get(i);
            
            String occupiedStatus = t.isOccupied() ? "BUSY" : "FREE";
            String vipStatus      = t.isVIP() ? "* YES" : "NO";
            
            // Logic for "Free" vs Price display
            String priceDisplay = (t.getBasePrice() == 0) ? "Free" : String.format("$%.2f", t.getBasePrice());

            System.out.printf("| %-5d | %-8d | %-14s | %-10s | %-8s | %-9s |\n",
                    i + 1, 
                    t.getCapacity(), 
                    t.getLocation(), 
                    occupiedStatus, 
                    vipStatus,
                    priceDisplay);
        }
        
        System.out.println(separator);
        System.out.println("Total Inventory Count: " + tables.size() + "\n");
    }
}