package example.todo.service;

import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;

import java.util.Scanner;

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

}
