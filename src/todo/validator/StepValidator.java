package example.todo.validator;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Step;
import example.todo.entity.Task;

public class StepValidator implements db.Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Input is not instance of Step");
        }

        Step step = (Step) entity;
        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Step title can't be empty or null");
        }

        try {
            Task task = (Task) Database.findById(step.getTaskRef(), step.getEntityCode());
        } catch (EntityNotFoundException e) {
            throw new InvalidEntityException("Step's task does not exist");
        }
    }
}