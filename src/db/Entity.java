package db;

public abstract class Entity implements Cloneable {
    public int id;

    @Override
    protected Entity clone() throws CloneNotSupportedException{
        return (Entity) super.clone();
    }

    public abstract int getEntityCode();

}