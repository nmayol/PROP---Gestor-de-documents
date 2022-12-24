package Exceptions;

public class ExceptionUsuariNoExisteix extends Exception {

    public ExceptionUsuariNoExisteix(String nom){
        super("No existeix l'usuari amb aquest nom");
    }
}
