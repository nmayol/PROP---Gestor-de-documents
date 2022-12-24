package Exceptions;

public class ExceptionIO extends Exception{
    public ExceptionIO(String motiu){
        super("Error en disc: " + motiu);
    }
}
