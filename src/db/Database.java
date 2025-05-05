package db;

import db.exception.EntityNotFoundException;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Database {
    private static ArrayList<db.Entity> entities = new ArrayList<>();
    private static HashMap<Integer, db.Validator> validators = new HashMap<>();
    private static int indexId = 1;
    private static List<Step> steps = new ArrayList<>();

    public static List<Step> getSteps() {
        return steps;
    }

    public static void delete(int ID) {
        int flag = 0;
        for (db.Entity e : entities) {
            if (e.id == ID) {
                entities.remove(e);
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            throw new EntityNotFoundException(ID);
        }
    }

    private Database() {}

    public static void registerValidator(int entityCode, db.Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for entity code " + entityCode + " already exist");
        }
        validators.put(entityCode , validator);
    }

    public static db.Entity add(db.Entity e) throws InvalidEntityException {
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
            return clone;
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



    public static void update(db.Entity e) throws EntityNotFoundException, InvalidEntityException {
        db.Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        int index = -1;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntityNotFoundException("Entity with ID " + e.id + " not found in the list.");
        }

        try {
            entities.set(index, (db.Entity) e.clone());
        } catch (CloneNotSupportedException ea) {
            throw new RuntimeException("Cloning failed", ea);
        }

        if (e instanceof db.Trackable) {
            Date now = new Date();
            ((db.Trackable) e).setLastModificationDate(now);
        }

        System.out.println("Entity updated successfully.");
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

    public static ArrayList<db.Entity> getAll(int entityCode) throws CloneNotSupportedException {
        ArrayList<db.Entity> result = new ArrayList<>();
        for (db.Entity e : entities) {
            if (e.getEntityCode() == entityCode) {
                result.add(e.clone());
            }
        }
        return result;
    }
}