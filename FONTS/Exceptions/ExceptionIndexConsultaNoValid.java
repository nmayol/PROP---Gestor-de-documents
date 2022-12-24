package Exceptions;

public class ExceptionIndexConsultaNoValid extends Exception{

    public ExceptionIndexConsultaNoValid(int k){
        super("Numero de consulta no valid: " + k);
    }
}
