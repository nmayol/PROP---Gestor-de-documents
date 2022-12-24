package Exceptions;

public class ExceptionDirectoriNoCreat extends Exception{
    public ExceptionDirectoriNoCreat(String dir){
        super("No s'ha pogut crear el directori " + dir);
    }
}
