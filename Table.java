public class Table {

    private String tableId;
    private int capacity;
    private boolean isOccupied;
    private String location;

    public Table(String tableId, int capacity, boolean isOccupied, String location) {
        setTableId(tableId);
        setCapacity(capacity);
        setOccupied(isOccupied);
        setLocation(location);
    }

    public String getTableId() {
        return tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public String getLocation() {
        return location;
    }

    public void setTableId(String tableId) {
        if (tableId == null || tableId.trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID cannot be empty");
        }
        this.tableId = tableId;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
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

    public String getStatus() {
        return isOccupied ? "Occupied" : "Available";
    }

    public String getTableInfo() {
        return String.format("Table %s (%s) - Capacity: %d - %s", 
            tableId, location, capacity, getStatus());
    }
}