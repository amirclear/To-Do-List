package example;

import db.Database;
import db.exception.EntityNotFoundException;
import example.Human;
import example.db.exception.InvalidEntityException;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        Database.registerValidator(Human.HUMAN_ENTITY_CODE, new example.HumanValidator());

        Human ali = new Human("Ali", -10);
        Database.add(ali);
    }
}