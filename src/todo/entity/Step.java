package example.todo.entity;

import java.util.Date;

public class Step extends db.Entity {

    public enum Status {NotStarted,Completed};

    private String title;
    private Status status;
    private int taskRef;
    private Date creationDate;
    private Date lastModificationDate;

    public static final int STEP_ENTITY_CODE = 5;

    public Step (String title,int taskRef) {
        this.title = title;
        this.status = Status.NotStarted;
        this.taskRef = taskRef;
        this.creationDate = new Date();
    }

    @Override
     public int getEntityCode() {
        return STEP_ENTITY_CODE;
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

    public int getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(int taskRef) {
        this.taskRef = taskRef;
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

    @Override
    public String toString() {
        return "  + ID: " + id +
                "\n    Title: " + title +
                "\n    Status: " + status +
                "\n    Creation Date: " + getCreationDate() +
                "\n    Last Modification Date: " + getLastModificationDate();
    }

}
