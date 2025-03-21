package db.exception;

public class EntityNotFoundException extends Exception{

    EntityNotFoundException(){
        System.out.println("Cannot find entity");
    }

    EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(int id){
        System.out.println("Cannot find entity with id= " + id);
    }
}
