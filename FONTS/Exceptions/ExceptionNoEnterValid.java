package Exceptions;

public class ExceptionNoEnterValid extends Exception{

    public ExceptionNoEnterValid(int k) {
        super("Enter " + k + " no valid");
    }
}
