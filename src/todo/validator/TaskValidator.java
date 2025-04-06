package example.todo.validator;

import db.Entity;
import example.db.exception.InvalidEntityException;
import example.todo.entity.Task;

public class TaskValidator implements db.Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if ( entity instanceof Task) {
            Task task = (Task) entity;
            if (task.getTitle() == null ||  task.getTitle().isEmpty()) {
                throw new IllegalArgumentException("Task title can't be empty or null");
            }
        }
        else {
            throw new IllegalArgumentException("Input is not a Task");
        }
    }

}
