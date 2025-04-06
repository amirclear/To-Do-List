package db;

import db.exception.EntityNotFoundException;
import example.db.exception.InvalidEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

        db.Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof db.Trackable) {
            Date now = new Date();
            ((db.Trackable) e).setCreationDate(now);
            ((db.Trackable) e).setLastModificationDate(now);
        }

        try {
            db.Entity clone = (db.Entity) e.clone();
            clone.id = indexId++;
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

    public static void update(db.Entity e) throws EntityNotFoundException, InvalidEntityException {
        db.Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        db.Entity existing = get(e.id);
        try {
            entities.set(entities.indexOf(existing), (db.Entity) e.clone());
        } catch (CloneNotSupportedException ea) {
            throw new RuntimeException("Cloning failed", ea);
        }

        if (e instanceof db.Trackable) {
            Date now = new Date();
            ((db.Trackable) e).setLastModificationDate(now);
        }
    }

    public static db.Entity findById(int id, int entityCode) throws EntityNotFoundException {
        for (db.Entity e : entities) {
            if (e.id == id && e.getEntityCode() == entityCode) {
                try {
                    return (db.Entity) e.clone();
                } catch (CloneNotSupportedException ex) {
                    throw new RuntimeException("Clone failed", ex);
                }
            }
        }
        throw new EntityNotFoundException(id);
    }
}