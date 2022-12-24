package Exceptions;

public class ExceptionNoSessioActiva extends Exception {

    public ExceptionNoSessioActiva(){
        super("No hi ha cap sessio activa");
    }
}
