package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int indexId = 1;

    private Database() {}

    public static void add(Entity e) {
        if ( e == null ) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        e.id = indexId++;
        entities.add(e);
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        Entity e = get(id);
        entities.remove(e);
    }

    public static void update(Entity updatedEntity) throws EntityNotFoundException {
        Entity oldEntity = get(updatedEntity.id);
        int index = entities.indexOf(oldEntity);
        entities.set(index, updatedEntity);
    }
}