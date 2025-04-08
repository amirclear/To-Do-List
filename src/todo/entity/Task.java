package example.todo.entity;

import java.util.Date;

import db.Trackable;
import db.Entity;


public class Task extends Entity implements Trackable {


    public enum Status {Completed,InProgress,NotStarted};

    private final int ENTITY_CODE = 10;

    private String title;
    private String description;
    private Date dueDate;
    private Status status;
    private Date creationDate;
    private Date lastModifiedDate;

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.NotStarted;
        this.creationDate = new Date();
        this.lastModifiedDate = new Date();
    }


    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
        updateModifiedDate();
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModifiedDate = date;
        updateModifiedDate();
    }

    @Override
    public Date getLastModificationDate() {
        return lastModifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateModifiedDate();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        updateModifiedDate();
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        updateModifiedDate();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        updateModifiedDate();
    }

    public void updateModifiedDate() {
        this.lastModifiedDate = new Date();
    }

    @Override
    public int getEntityCode() {
        return ENTITY_CODE;
    }


}
