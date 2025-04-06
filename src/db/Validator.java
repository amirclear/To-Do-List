package db;
import db.Entity;
import example.db.exception.InvalidEntityException;

public interface Validator {
    void validate(Entity entity) throws InvalidEntityException;

}
