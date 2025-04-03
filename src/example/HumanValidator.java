package example;
import db.Entity;
import db.Validator;
import example.db.exception.InvalidEntityException;

public class HumanValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if ( !(entity instanceof example.Human)) {
            throw new IllegalArgumentException("Invalid entity type: Expected Human");
        }

        example.Human human = (example.Human) entity;

        if ( human.age < 0) {
            throw new InvalidEntityException("Age must be positive integer");
        }
        if ( human.name == null || human.name.isEmpty()) {
            throw new InvalidEntityException("Name cannot be null or empty");
        }
    }
}
