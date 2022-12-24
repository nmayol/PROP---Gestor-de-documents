package Exceptions;

public class ExceptionAutorJaExisteix extends Exception {

    public ExceptionAutorJaExisteix(String nom) {
        super("L'autor " + nom + " ja existeix");
    }
}
