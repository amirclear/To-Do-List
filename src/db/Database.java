package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static ArrayList<db.Entity> entities = new ArrayList<>();
    private static HashMap<Integer, db.Validator> validators = new HashMap<>();
    private static int indexId = 1;

    private Database() {}

    public static void add(db.Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        try {
            db.Entity copy = (db.Entity) e.clone();
            copy.id = indexId++;
            entities.add(copy);
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Clone not supported", ex);
        }
    }

    public static db.Entity get(int id) throws EntityNotFoundException {
        for (db.Entity e : entities) {
            if (e.id == id) {
                try {
                    return (db.Entity) e.clone();
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException("Clone not supported", ex);
                }
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        db.Entity e = get(id);
        entities.removeIf(entity -> entity.id == id);
    }

    public static void update(db.Entity e) throws EntityNotFoundException {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                try {
                    entities.set(i, (db.Entity) e.clone());
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException("Clone not supported", ex);
                }
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}