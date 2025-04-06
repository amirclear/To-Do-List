package example.todo.entity;

public class Step extends db.Entity {

    public enum Status {NotStarted,Completed};
    private String title;
    private Status status;
    private int tastRef;
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

}
