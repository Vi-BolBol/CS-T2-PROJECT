package models;
public class Table {

    private String tableId;
    private int capacity;
    private boolean isOccupied;
    private String location;

    public Table(String tableId, int capacity, boolean isOccupied, String location) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
        this.location = location;
    }

    public String getTableInfo() {
        return String.format("Table %s (%s) - Capacity: %d - %s", 
            tableId, location, capacity, isOccupied);
    }
}