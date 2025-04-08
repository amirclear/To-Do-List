package example.todo.service;

import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.entity.Task;

import javax.xml.crypto.Data;
import java.util.Scanner;

import static example.todo.entity.Step.*;
import static example.todo.service.TaskService.sc;

public class StepService {

    public static void saveStep(int taskRef, String title) throws InvalidEntityException {
        System.out.println("Enter the ID number of Task to add Step : ");
        Scanner sc = new Scanner(System.in);
        int taskID = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the Title of Step : ");
        String stepTitle = sc.nextLine();

        Step step = new Step(stepTitle,taskID);
        db.Database.add(step);
        System.out.println("Step Added\nID : " + taskID);
    }

    public static void addStep() throws InvalidEntityException {
        System.out.println("Enter the Id of Step:");
        int taskID = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Title of Step: ");
        String title = sc.nextLine();
        Step step = new Step(title,taskID);
        db.Database.add(step);
        System.out.println("Step saved successfully.\nTaskID: " + step.id);
        System.out.println("Creation Date: " + step.getCreationDate());
    }

    public static void delete(int id) throws CloneNotSupportedException {
        db.Entity entity;

        try {
            entity = db.Database.get(id);
        } catch (db.exception.EntityNotFoundException e) {
            System.out.println("Error: No entity found with ID " + id);
            return;
        }

        if (!(entity instanceof Step)) {
            System.out.println("Error: The ID does not correspond to a Step.");
            return;
        }


        try {
            db.Database.delete(id);
            System.out.println("Step deleted successfully.");
        } catch (db.exception.EntityNotFoundException e) {
            System.out.println("Error: Failed to delete Step :  Entity not found.");
        }
    }

    public static void editTitle(int id) throws InvalidEntityException {
        Step step = (Step) db.Database.get(id);
        System.out.println("Enter the new value: ");
        String newField = sc.nextLine();
        String oldField = step.getTitle();
        step.setTitle(newField);
        db.Database.update(step);
        System.out.println("Successfully updated the task.\n" + "Field: title");
        System.out.println("Old Value: " + oldField +"\nNew Value: " + newField);
        System.out.println("Modification Date: " + step.getLastModificationDate());
    }

    public static void setAsCompleted(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        Step step = (Step) db.Database.get(taskId);
        Status previousStatus = step.getStatus();
        step.setStatus(Status.Completed);
        db.Database.update(step);
        System.out.println("Task successfully updated");
        System.out.println("Field : status");
        System.out.println("Old value : " + previousStatus);
        System.out.println("new value : " + step.getStatus());
        System.out.println("last modified date : " + step.getLastModificationDate());
        for (db.Entity e : db.Database.getAll(step.getEntityCode())) {
            if (((Step) e).getTastRef() == taskId) {
                ((Step) e).setStatus(Status.Completed);
            }
        }
    }

    public static void setAsNotStarted(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        Step step = (Step) db.Database.get(taskId);
        Status previousStatus = step.getStatus();
        step.setStatus(Status.NotStarted);
        db.Database.update(step);
        System.out.println("Task successfully updated");
        System.out.println("Field : status");
        System.out.println("Old value : " + previousStatus);
        System.out.println("New value : " + step.getStatus());
        System.out.println("last modified date : " + step.getLastModificationDate());
    }

    public static void editStatus(int id) throws InvalidEntityException, CloneNotSupportedException {
        String newStatus = sc.nextLine();
        switch (newStatus) {
            case "Completed": {
                setAsCompleted(id);
                checkStatus((Step) db.Database.get(id));
                break;
            }
            case "NotStarted": {
                setAsNotStarted(id);
                break;
            }
            default: {
                System.out.println("Invalid step status");
                break;
            }
        }
    }

    public static void checkStatus(Step step) throws CloneNotSupportedException, InvalidEntityException {
        int flag = 1;

        for (db.Entity e : db.Database.getAll(step.getEntityCode()))  {
            Step o = (Step) e;
            if (o.getTastRef() == step.getTastRef()) {
                if (o.getStatus() != Status.Completed) {
                    flag = 0;
                }
            }

            Task task = (Task) db.Database.get(step.getTastRef());
            if (flag == 1) {
                task.setStatus(Task.Status.Completed);
            }
            if (task.getStatus() == Task.Status.NotStarted) {
                task.setStatus(Task.Status.InProgress);
            }

            db.Database.update(task);
        }
    }

}
