package Exceptions;

public class ExceptionUsuariJaExisteix extends Exception {

    public ExceptionUsuariJaExisteix(String nom){
        super("Ja existeix l'usuari amb nom " + nom);
    }
}
