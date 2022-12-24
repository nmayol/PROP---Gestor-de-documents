package Exceptions;

public class ExceptionDirectoriNoEliminat extends Exception{
    public ExceptionDirectoriNoEliminat(String dir){
        super("No s'ha pogut eliminar el directori " + dir);
    }
}
