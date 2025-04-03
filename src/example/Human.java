package example;

import db.Entity;

public class Human extends Entity implements Cloneable {
    public String name;
    public int age;
    public static final int HUMAN_ENTITY_CODE = 14;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
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

    @Override
    public int getEntityCode() {
        return HUMAN_ENTITY_CODE;
    }

}