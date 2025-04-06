package example.todo.entity;

import java.util.Date;

public class Step extends db.Entity {

    public enum Status {NotStarted,Completed};
    private String title;
    private Status status;
    private int tastRef;
    private Date creationDate;
    private Date lastModificationDate;
    private final int Entity_Code = 10;

    public Step (String title,int taskRef) {
        this.title = title;
        this.status = Status.NotStarted;
        this.tastRef = taskRef;
    }

    @Override
     public int getEntityCode() {
        return Entity_Code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTastRef() {
        return tastRef;
    }

    public void setTastRef(int tastRef) {
        this.tastRef = tastRef;
    }

    @Override
    public String toString() {
        return "Title : " + getTitle() + "\nID : " + id + "\nStatus : " + getStatus();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

}
