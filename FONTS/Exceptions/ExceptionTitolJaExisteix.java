package Exceptions;

public class ExceptionTitolJaExisteix extends Exception {

    public ExceptionTitolJaExisteix(String nom) {
        super("El titol " + " ja existeix");
    }
}
