package Exceptions;

public class ExceptionAutorNoExisteix extends Exception {
    public ExceptionAutorNoExisteix(String nom) {
        super("L'autor " + nom + " no existeix");
    }
}
