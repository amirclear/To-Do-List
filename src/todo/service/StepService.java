package example.todo.service;

import db.Entity;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.entity.Task;
import db.Database;

import java.util.List;

import static example.todo.entity.Step.*;
import static example.todo.service.TaskService.sc;

public class StepService {

    public static void addStep() throws InvalidEntityException {
        System.out.println("Enter the Id of Task: ");
        int taskID = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Title of Step: ");
        String title = sc.nextLine();
        Step step = new Step(title, taskID);
        Step savedStep = (Step) db.Database.add(step);
        System.out.println("Step saved successfully.\nStep ID: " + savedStep.id);
        System.out.println("Creation Date: " + savedStep.getCreationDate());
    }

    public static void deleteStep(int id) throws InvalidEntityException, CloneNotSupportedException {
        for (db.Entity e : Database.getAll(Step.STEP_ENTITY_CODE)) {
            if (e instanceof Step) {
                if (((Step) e).getTaskRef() == id) {
                    Database.delete(e.id);
                }
            }
        }
    }

    public static void deleteOnlyStep() {

        System.out.println("Task ID: ");
        int taskRef = sc.nextInt();
        System.out.println("Step ID: ");
        int id = sc.nextInt();
        try {
            Step step = (Step) Database.get(id);
            if (step.getTaskRef() == taskRef) {
                Database.delete(id);
                System.out.println("Step: " + id + " deleted successfully");
            } else {
                throw new db.exception.EntityNotFoundException();
            }
        } catch (
                db.exception.EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("the entity is not a step");
        }

    }

    private static Step getStepById(int id) {
        for (Step step : db.Database.getSteps()) {
            if (step.id == id) {
                return step;
            }
        }
        return null;
    }

    public static void updateStep() {
        System.out.println("ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            db.Entity e = Database.get(id);
            Step step = (Step) e;

            System.out.println("Field (title, status(NOT_STARTED, COMPLETED)):");
            String field = sc.nextLine().toLowerCase();
            System.out.println("New Value: ");
            String value = sc.nextLine();

            String oldValue = "";
            boolean updated = false;

            switch (field) {
                case "title":
                    oldValue = step.getTitle();
                    step.setTitle(value);
                    updated = true;
                    break;
                case "status":
                    oldValue = step.getStatus().toString();
                    String normalizedValue = value.trim().toLowerCase();
                    if (normalizedValue.equals("completed") || normalizedValue.equals("not_started")) {
                        String enumName = normalizedValue.equals("completed") ? "Completed" : "NotStarted";
                        step.setStatus(Step.Status.valueOf(enumName));
                        updated = true;
                    }
                     else {
                        throw new IllegalArgumentException("Invalid status value");
                    }
                    break;
                default:
                    System.out.println("Invalid field");
            }

            if (updated) {
                Database.update(step);

                db.Entity taskEntity = Database.get(step.getTaskRef());
                Task task = (Task) taskEntity;

                Step stepTemplate = new Step("", 0);
                List<Entity> steps = Database.getAll(stepTemplate.getEntityCode());
                boolean allCompleted = true;
                boolean anyCompleted = false;

                for (db.Entity s : steps) {
                    Step st = (Step) s;
                    if (st.getTaskRef() == task.id) {
                        if (st.getStatus() != Step.Status.Completed) {
                            allCompleted = false;
                        } else {
                            anyCompleted = true;
                        }
                    }
                }

                if (allCompleted) {
                    if (task.getStatus() != Task.Status.Completed) {
                        task.setStatus(Task.Status.Completed);
                        Database.update(task);
                    }
                }
                else if (anyCompleted && task.getStatus() == Task.Status.NotStarted) {
                    task.setStatus(Task.Status.InProgress);
                    Database.update(task);
                }

                System.out.println("Successfully updated the step.");
                System.out.println("Field: " + field);
                System.out.println("Old Value: " + oldValue);
                System.out.println("New Value: " + value);
                System.out.println("Modification Date: " + step.getLastModificationDate());
            }
        } catch (InvalidEntityException | db.exception.EntityNotFoundException e) {
            System.out.println("Cannot update step with ID=" + id + ".");
            System.out.println("Error: " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("The entity is not a step.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
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
            if (((Step) e).getTaskRef() == taskId) {
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
            if (o.getTaskRef() == step.getTaskRef()) {
                if (o.getStatus() != Status.Completed) {
                    flag = 0;
                }
            }

            Task task = (Task) db.Database.get(step.getTaskRef());
            if (flag == 1) {
                task.setStatus(Task.Status.Completed);
            }
            if (task.getStatus() == Task.Status.NotStarted) {
                task.setStatus(Task.Status.InProgress);
            }

            db.Database.update(task);
        }
    }

    public static void getStep(int taskRef) throws CloneNotSupportedException {
        int flag = 0;
        for(db.Entity e: Database.getAll(Step.STEP_ENTITY_CODE)){
            Step step = (Step) e;
            if(step.getTaskRef() == taskRef){
                System.out.println("  + ID: " + step.id);
                System.out.println("    Title: " + step.getTitle());
                System.out.println("    Status: " + step.getStatus());
                flag = 1;
            }
        }
        if(flag == 0){
            System.out.println("this task doesn't have any steps");
        }
    }

}
