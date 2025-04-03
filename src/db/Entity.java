package db;

public abstract class Entity implements Cloneable {
    public int id;

    public abstract Entity clone();
}