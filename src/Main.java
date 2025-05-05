package example;

import example.db.exception.InvalidEntityException;
import example.todo.service.StepService;
import example.todo.service.TaskService;

import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InvalidEntityException {

        Scanner sc = new Scanner(System.in);
        String process;
        System.out.println("--Welcome to TODO LIST--\n\n");

        do {
            System.out.println("Enter your Choice\n(add task - add step - delete task - update task - delete step - update step -"+
                    " get task-by-id - get all-tasks - get incomplete tasks - exit\n");
            System.out.print("Enter Here: ");
            process = sc.nextLine().toLowerCase().trim();

            switch (process) {
                        case "add task": {
                            try {
                                TaskService.addTask();
                            }
                            catch (ParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            } catch (InvalidEntityException | IllegalArgumentException e) {
                                System.out.println("Cannot save task. Error: Task Title can't be empty");
                            }
                            break;
                        }
                        case "add step": {
                            try {
                                StepService.addStep();
                            } catch (InvalidEntityException | IllegalArgumentException e) {
                                System.out.println("Cannot save step. Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "delete task": {
                            TaskService.deleteTask();
                            break;
                        }

                        case "update task": {
                                TaskService.updateTask();
                                    break;
                        }

                        case "update step": {
                        StepService.updateStep();
                        break;
                        }

                        case "delete step":
                            StepService.deleteOnlyStep();
                            break;

                        case "get task-by-id" :{
                            try {
                                System.out.println("Enter the ID: ");
                                int ID = sc.nextInt();
                                sc.nextLine();
                                TaskService.getTask(ID);
                            } catch (CloneNotSupportedException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        }

                       case "get all-tasks" : {
                           try {
                               TaskService.getAll();
                           } catch (CloneNotSupportedException e) {
                               System.out.println(e.getMessage());
                           }
                           break;
                       }

                       case "get incomplete tasks": {
                            try {
                                TaskService.getIncompleteTask();
                            } catch (CloneNotSupportedException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        }

                        case "exit" : {
                            System.exit(0);
                        }

                        default : {
                            System.out.println("Input is invalid\n"); continue;
                        }
        } }while (!Objects.equals(process, "exit"));
    }
}