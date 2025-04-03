package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;

public class Database {
    private static ArrayList<db.Entity> entities = new ArrayList<>();
    private static int indexId = 1;

    private Database() {}

    public static void add(db.Entity e) {
        if ( e == null ) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        db.Entity copy = e.copy();
        copy.id = indexId++;
        entities.add(copy);
    }

    public static db.Entity get(int id) throws EntityNotFoundException {

        for (db.Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        db.Entity e = get(id);
        entities.remove(e);
    }

    public static void update(db.Entity e) throws EntityNotFoundException {

        db.Entity existing = get(e.id);
        entities.set(entities.indexOf(existing), e.copy());

    }
}