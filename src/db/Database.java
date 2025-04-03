package db;
import db.Validator;
import db.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import db.Entity;
import example.db.exception.InvalidEntityException;

public class Database {
    private static ArrayList<db.Entity> entities = new ArrayList<>();
    private static HashMap<Integer, db.Validator> validators = new HashMap<>();
    private static int indexId = 1;

    private Database() {}

    public static void registerValidator(int entityCode, db.Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for entity code " + entityCode + " already exist");
        }
        validators.put(entityCode , validator);
    }

    public static void add(db.Entity e) throws InvalidEntityException {
        if (e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Validator validator = validators.get(e.getEntityCode());
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for entity code " + e.getEntityCode());
        }
        validator.validate(e);
        try {
            Entity clone = (Entity) e.clone();
            clone.id = indexId;
            indexId++;
            entities.add(clone);
        } catch (CloneNotSupportedException ea) {
            throw new RuntimeException("Cloning failed", ea);
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