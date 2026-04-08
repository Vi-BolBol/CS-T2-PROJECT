package db;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base entity class for all persistable objects.
 * Demonstrates ENCAPSULATION: private id/createdAt with read-only access.
 * Demonstrates INHERITANCE: all domain models extend this.
 */
public abstract class DatabaseObject {

    private final String id;
    private final LocalDateTime createdAt;

    protected DatabaseObject() {
        this.id        = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // Read-only — identity must never change after creation
    public String getId()              { return id; }
    public LocalDateTime getCreatedAt(){ return createdAt; }

    /**
     * ABSTRACTION: every entity must be able to describe itself.
     */
    public abstract String getSummary();
}
