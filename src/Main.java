package example;

import db.exception.EntityNotFoundException;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.service.StepService;
import example.todo.service.TaskService;

import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InvalidEntityException {

        Scanner sc = new Scanner(System.in);
        String process;

        do {
            System.out.println("Enter your Choice\n(add task - add step - delete - update task - update step -"+
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

                case "delete": {
                    System.out.println("Enter the ID to delete: ");
                    int ID = sc.nextInt();
                    sc.nextLine();
                    try {
                        StepService.delete(ID);
                    }
                    catch (EntityNotFoundException | CloneNotSupportedException e) {
                        System.out.println("Cannot delete entity with ID=" + ID);
                        System.out.println("Error: Something happend");
                    }
                    break;
                }

                case "update task": {
                    System.out.println("Enter ID of Task: ");
                    int ID = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the Field wants to edit: (title/description/status/due date)");
                    String editChoice = sc.nextLine().toLowerCase().trim();
                    switch (editChoice) {
                        case "title": {
                            try {
                                TaskService.editTitle(ID);
                            }
                            catch (InvalidEntityException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "description": {
                            try {
                                TaskService.editDescription(ID);
                            }
                            catch (InvalidEntityException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "status": {
                            try {
                                TaskService.editStatus(ID);
                            } catch (InvalidEntityException | CloneNotSupportedException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch ( IllegalArgumentException e ) {
                                System.out.println("Invalid task status\nchoose from (Completed, NotStarted, InProgress)");
                            }
                            break;
                        }

                        case "due date": {
                            try {
                                TaskService.editDueDate(ID);
                            } catch (InvalidEntityException | ParseException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        }
                    }
                }

                case "update Step": {
                    System.out.println("Enter ID of Step: ");
                    int ID = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the Field wants to edit: (title/status/exit)");
                    String editChoice = sc.nextLine().toLowerCase().trim();
                    switch (editChoice) {
                        case "title": {
                            try {
                                StepService.editTitle(ID);
                            }
                            catch (InvalidEntityException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "status": {
                            try {
                                StepService.editStatus(ID);
                            } catch (InvalidEntityException | CloneNotSupportedException e) {
                                System.out.println("Cannot update task with ID=" + ID);
                                System.out.println("Error: " + e.getMessage());
                            }
                            catch ( IllegalArgumentException e ) {
                                System.out.println("Invalid task status\nchoose from (Completed, NotStarted, InProgress)");
                            }
                            break;
                        }
                        case "exit":
                            System.out.println(" Exiting Step update.");
                            return;

                        default:
                            System.out.println(" Unknown option. Try again.");
                            break;
                        }

                        }

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
                            System.out.println("Input is invalid"); continue;
                        }
        } }while (!Objects.equals(process, "exit"));

    }
}