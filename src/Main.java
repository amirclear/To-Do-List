package example;

import db.Database;
import db.exception.EntityNotFoundException;
import example.Human;

public class Main {
    public static void main(String[] args) {
        Human ali = new Human("Ali");

        Database.add(ali);

        ali.name = "Ali Hosseini";

        try {
            Human aliFromDatabase = (Human) Database.get(1);
            System.out.println("ali's name in the database: " + aliFromDatabase.name);
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}