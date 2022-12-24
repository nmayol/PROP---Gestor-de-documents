package Exceptions;

public class ExceptionConsultaLimitParametres extends Exception{
    public ExceptionConsultaLimitParametres(){
        super("No es poden afegir mes parametres a la consulta");
    }
}
