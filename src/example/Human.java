package example;

import db.Entity;

public class Human extends Entity implements Cloneable {
    public String name;
    public int age;

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
}