package db;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Map<String, DatabaseObject> storage = new HashMap<>();

    // CREATE
    public void create(DatabaseObject obj) {
        storage.put(obj.getId(), obj);
    }
    
    
    // READ (by ID)
    public DatabaseObject readById(String id) {
        DatabaseObject obj = storage.get(id);
        
        if (obj == null) {
            return null;
        }

        return obj;
    }
    
    // UPDATE
    public void update(String id, DatabaseObject updatedObj) {
        
        if (!storage.containsKey(id)) {
            System.out.println("Cannot update: Object not found");
            return;
        }

        try {
            storage.put(id, updatedObj);
            System.out.println(" Object updated successfully!");
        } catch (Exception e) {
            System.out.println(" Update failed: " + e.getMessage());
        }
    }
    
    // DELETE 
    public void Delete(String id) {
        
        if (storage.remove(id) != null) {
            System.out.println("Object permanently removed");
        } else {
            System.out.println("Object not found");
        }
    }
    
    // FILTER BY TYPE
    public <T extends DatabaseObject> List<T> filterByType(Class<T> type) {
        List<T> results = new ArrayList<>();
        
        for (DatabaseObject obj : storage.values()) {
            if ( type.isInstance(obj)) {
                results.add(type.cast(obj));
            }
        }
        
        return results;
    }
    
}
