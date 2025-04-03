package example;

import db.Entity;

public class Human extends Entity implements Cloneable {
    public String name;

    public Human(String name) {
        this.name = name;
    }

    @Override
    public Human clone() {
        try {
            Human copy = (Human) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}