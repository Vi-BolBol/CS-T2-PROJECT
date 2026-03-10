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

    // READ (by ID) - generic overload that returns a specific subclass
    public <T extends DatabaseObject> T readById(String id, Class<T> type) {
        DatabaseObject obj = storage.get(id);

        if (obj == null) {
            return null;
        }

        if (!type.isInstance(obj)) {
            System.out.println("Object found is not of type " + type.getSimpleName());
            return null;
        }

        return type.cast(obj);
    }
    
    // UPDATE
    public void update(String id, DatabaseObject updatedObj) {
        
        if (!storage.containsKey(id)) {
            System.out.println("Cannot update: Object not found");
            return;
        }

        try {
            storage.put(id, updatedObj);
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

    //<T extends DatabaseObject> : whatever T is, it must be a subclass of DatabaseObj
    // Class<T> type: by passing type.class, you giving method the physical object that it can use to check type while the programming is running
    // type.isInstance(obj): ask the JVM that is the obj actually the instance of type ?
    // type.cast(obj): cast obj in T type
    
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
