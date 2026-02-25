package db;
import java.util.*;
import java.time.LocalDateTime;

public class DatabaseObject {

    private String id;
    private LocalDateTime createdAt;
    
    public DatabaseObject() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
    
    public String getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
