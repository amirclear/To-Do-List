package db;

public abstract class Entity implements Cloneable {
    public int id;

    @Override
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public abstract int getEntityCode();

}