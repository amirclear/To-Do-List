package example.todo.service;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.entity.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class TaskService {
    static Scanner sc = new Scanner(System.in);

    public static void setAsCompleted(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        Task task = (Task) db.Database.get(taskId);
        Task.Status previousStatus = task.getStatus();
        task.setStatus(Task.Status.Completed);
        db.Database.update(task);
        System.out.println("Task successfully updated");
        System.out.println("Field : status");
        System.out.println("Previous value : " + previousStatus);
        System.out.println("Old value : " + task.getStatus());
        System.out.println("last modified date : " + task.getLastModificationDate());
        Step step = new Step("",0);
        for (db.Entity e : db.Database.getAll(step.getEntityCode())) {
            if (((Step) e).getTaskRef() == taskId) {
                ((Step) e).setStatus(Step.Status.Completed);
            }
        }
    }

    public static void setAsNotStarted(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        Task task = (Task) db.Database.get(taskId);
        Task.Status previousStatus = task.getStatus();
        task.setStatus(Task.Status.NotStarted);
        db.Database.update(task);
        System.out.println("Task successfully updated");
        System.out.println("Field : status");
        System.out.println("Old value : " + previousStatus);
        System.out.println("New value : " + task.getStatus());
        System.out.println("last modified date : " + task.getLastModificationDate());
    }

    public static void setAsInProgress(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        Task task = (Task) db.Database.get(taskId);
        Task.Status previousStatus = task.getStatus();
        task.setStatus(Task.Status.InProgress);
        db.Database.update(task);
        System.out.println("Task successfully updated");
        System.out.println("Field : status");
        System.out.println("Old value : " + previousStatus);
        System.out.println("New value : " + task.getStatus());
        System.out.println("last modified date : " + task.getLastModificationDate());
    }

    public static void addTask() throws InvalidEntityException, ParseException {
        System.out.println("\nEnter Title of task : ");
        String title = sc.nextLine();
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Title cannot be empty");
            return;
        }

        System.out.println("Enter Task Description : ");
        String description = sc.nextLine();
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Description cannot be empty");
            return;
        }

        Date dueDate = null;
        while (dueDate == null) {
            System.out.println("Enter due Date (YYYY-MM-DD) : ");
            String inputDueDate = sc.nextLine();
            try {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(inputDueDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        Task task = new Task(title, description, dueDate);
        db.Database.add(task);
        System.out.println("Task saved successfully\nID: " + task.id + "\nCreation Date: " + task.getCreationDate());
        System.out.println();
    }

    public static void updateTask() {
        System.out.println("ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            db.Entity e = db.Database.get(id);
            Task task = (Task) e;

            System.out.println("Field (title, status(NOT_STARTED, IN_PROGRESS, COMPLETED)):");
            String field = sc.nextLine().toLowerCase();
            System.out.println("New Value: ");
            String value = sc.nextLine();

            String oldValue = "";
            boolean updated = false;

            switch (field) {
                case "title":
                    oldValue = task.getTitle();
                    task.setTitle(value);
                    updated = true;
                    break;
                case "status":
                    oldValue = task.getStatus().toString();
                    String normalizedValue = value.trim().toLowerCase();
                    switch (normalizedValue) {
                        case "completed":
                            task.setStatus(Task.Status.Completed);
                            break;
                        case "in_progress":
                            task.setStatus(Task.Status.InProgress);
                            break;
                        case "not_started":
                            task.setStatus(Task.Status.NotStarted);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid status value");
                    }
                    if (normalizedValue.equals("completed")) {
                        Step stepTemplate = new Step("", 0);
                        for (db.Entity stepEntity : db.Database.getAll(stepTemplate.getEntityCode())) {
                            Step step = (Step) stepEntity;
                            if (step.getTaskRef() == task.id && step.getStatus() != Step.Status.Completed) {
                                step.setStatus(Step.Status.Completed);
                                db.Database.update(step);
                            }
                        }
                    }
                    updated = true;
                    break;
                default:
                    System.out.println("Invalid field");
            }

            if (updated) {
                db.Database.update(task);
                System.out.println("Successfully updated the task.");
                System.out.println("Field: " + field);
                System.out.println("Old Value: " + oldValue);
                System.out.println("New Value: " + value);
                System.out.println("Modification Date: " + task.getLastModificationDate());
            }
        } catch (InvalidEntityException | db.exception.EntityNotFoundException e) {
            System.out.println("Cannot update task with ID=" + id + ".");
            System.out.println("Error: " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("The entity is not a task.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTask() {
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();

        try {
            db.Database.delete(id);
            StepService.deleteStep(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (db.exception.EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidEntityException e) {
            System.out.println("Cannot delete entity with ID=" + id + ".");
        }
        catch (ClassCastException e){
            System.out.println("the entity is not a task.");
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editTitle(int id) throws InvalidEntityException {
        Task task = (Task) db.Database.get(id);
        if (task == null) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }

        System.out.println("Enter the new value: ");
        String newField = sc.nextLine();
        if (newField == null || newField.trim().isEmpty()) {
            System.out.println("Title cannot be empty");
            return;
        }

        String oldField = task.getTitle();
        task.setTitle(newField);
        db.Database.update(task);
        System.out.println("Successfully updated the task.\n" + "Field: title");
        System.out.println("Old Value: " + oldField +"\nNew Value: " + newField);
        System.out.println("Modification Date: " + task.getLastModificationDate());
    }

    public static void editDescription(int id) throws InvalidEntityException {
        Task task = (Task) db.Database.get(id);
        System.out.println("Enter the new value: ");
        String newField = sc.nextLine();
        String oldField = task.getDescription();
        task.setDescription(newField);
        db.Database.update(task);
        System.out.println("Successfully updated the task.\n" + "Field: description");
        System.out.println("Old Value: " + oldField +"\nNew Value: " + newField);
        System.out.println("Modification Date: " + task.getLastModificationDate());
    }

    public static void editStatus(int id) throws CloneNotSupportedException, InvalidEntityException {
        System.out.println("Enter Task Status (Completed, NotStarted, InProgress): ");
        String newStatus = sc.nextLine().trim();
        if (!newStatus.equalsIgnoreCase("Completed") && !newStatus.equalsIgnoreCase("NotStarted") && !newStatus.equalsIgnoreCase("InProgress")) {
            System.out.println("Invalid task status. Please choose from (Completed, NotStarted, InProgress).");
            return;
        }

        switch (newStatus) {
            case "Completed": {
                setAsCompleted(id);
                break;
            }

            case "NotStarted": {
                setAsNotStarted(id);
                break;
            }

            case "InProgress": {
                setAsInProgress(id);
                break;
            }

        }

    }

    public static void editDueDate(int id) throws ParseException, InvalidEntityException {
        System.out.println("Enter the new due Date:");
        String newDueDate = sc.nextLine();
        Task task = (Task) db.Database.get(id);
        Date oldDueDate = task.getDueDate();
        task.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse(newDueDate));
        db.Database.update(task);
        System.out.println("Successfully updated the task.");
        System.out.println("Field: dueDate");
        System.out.println("old value: " + oldDueDate);
        System.out.println("new value: " + newDueDate);
    }

    public static void getTask(int id) throws CloneNotSupportedException {
        Task task = (Task) db.Database.get(id);
        System.out.println(task);
        System.out.println("Steps: ");
        int flag = 0;
        Step step = new Step("", 0);
        for (db.Entity e : db.Database.getAll(step.getEntityCode())) {
            Step o = (Step) e;
            if (o.getTaskRef() == id) {
                flag = 1;
                System.out.println(o);
            }
        }
        if (flag == 0) {
            System.out.println("No steps for this task");
        }
        System.out.println();
    }

    public static void getAll() throws CloneNotSupportedException {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(db.Entity e : db.Database.getAll(Task.ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        int flag = 0;
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
                System.out.println("ID: " + e.id);
                System.out.println("Title: " + e.getTitle());
                System.out.println("Description: " + e.getDescription());
                System.out.println("Due date: " + e.getDueDate());
                System.out.println("Status: " + e.getStatus());
                System.out.println("Steps: ");
                StepService.getStep(e.id);
                System.out.println();
                flag = 1;

        }
        if(flag == 0) {
            throw new IllegalArgumentException("No tasks found.");
        }
    }

    public static void getIncompleteTask() throws CloneNotSupportedException {
        ArrayList<Task> tasks = new ArrayList<>();
        for (db.Entity e : db.Database.getAll(Task.ENTITY_CODE)) {
            if (e instanceof Task) {
                Task task = (Task) e;
                if (task.getStatus() != Task.Status.Completed) {
                    tasks.add(task);
                }
            }
        }

        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
                System.out.println("Steps: ");
                StepService.getStep(task.id);
                System.out.println();
            }
        }
    }

}

