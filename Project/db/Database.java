package db;

import java.util.*;

/**
 * Generic in-memory store.
 * ENCAPSULATION: storage map is private; only exposed through typed methods.
 */
public class Database {

    private final Map<String, DatabaseObject> storage = new HashMap<>();

    // ── CREATE ────────────────────────────────────────────────────────────────

    public void create(DatabaseObject obj) {
        if (obj == null) throw new IllegalArgumentException("Cannot persist a null object.");
        storage.put(obj.getId(), obj);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    /** Returns null (not an exception) when the id is not found. */
    public <T extends DatabaseObject> T readById(String id, Class<T> type) {
        DatabaseObject obj = storage.get(id);
        if (obj == null || !type.isInstance(obj)) return null;
        return type.cast(obj);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public void update(String id, DatabaseObject updated) {
        if (!storage.containsKey(id)) {
            System.out.println("[DB] Update failed – object not found: " + id);
            return;
        }
        storage.put(id, updated);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    // ── QUERY ─────────────────────────────────────────────────────────────────

    /** Returns every object whose runtime type is exactly (or a subtype of) {@code type}. */
    public <T extends DatabaseObject> List<T> filterByType(Class<T> type) {
        List<T> results = new ArrayList<>();
        for (DatabaseObject obj : storage.values()) {
            if (type.isInstance(obj)) results.add(type.cast(obj));
        }
        return results;
    }
}
