package example.todo.service;

import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.entity.Task;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            if (((Step) e).getTastRef() == taskId) {
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
        System.out.println("Enter Task Description : ");
        String description = sc.nextLine();
        System.out.println("Enter due Date (YYYY-MM-DD) : ");
        String inputDueDate = sc.nextLine();
        Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(inputDueDate);
        Task task = new Task(title, description, dueDate);
        db.Database.add(task);
        System.out.println("Task saved successfully\nID: " + task.id + "\nCreation Date: " + task.getCreationDate());
        System.out.println("");
    }

    public static void editTitle(int id) throws InvalidEntityException {
        Task task = (Task) db.Database.get(id);
        System.out.println("Enter the new value: ");
        String newField = sc.nextLine();
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
        String newStatus = sc.nextLine();
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

            default: {
                System.out.println("Invalid task status\nchoose from (Completed, NotStarted, InProgress)");
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
        Step step = new Step("",0);
        for (db.Entity e : db.Database.getAll(step.getEntityCode())) {
            Step o = (Step) e;
            if (o.getTastRef() == id) {
                flag = 1;
                System.out.println(o);
            }
        }
        if (flag == 1) {
            System.out.println("No steps for this task");
        }
        System.out.println();
    }

    public static void getAll() throws CloneNotSupportedException {
        Step step = new Step("",0);
        ArrayList<db.Entity> tasks = db.Database.getAll(step.getEntityCode());

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                Task t1 = (Task) tasks.get(i);
                Task t2 = (Task) tasks.get(j);
                if (t1.getDueDate().after(t2.getDueDate())) {
                    db.Entity temp = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, temp);
                }
            }
        }

        for (db.Entity e : tasks) {
            Task task = (Task) e;
            System.out.println();
            getTask(task.id);
        }
    }

    public static void getIncompleteTask() throws CloneNotSupportedException {
        Step step = new Step("",0);
        for (db.Entity e : db.Database.getAll(step.getEntityCode())) {
            Task task = (Task) e;
            if (task.getStatus() != Task.Status.Completed) {
                System.out.println(task);
            }
        }
    }


}

