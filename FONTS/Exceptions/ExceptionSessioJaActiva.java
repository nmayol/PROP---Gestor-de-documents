package Exceptions;

public class ExceptionSessioJaActiva extends Exception {

    public ExceptionSessioJaActiva(){
        super("Ja hi ha una sessio activa");
    }
}
