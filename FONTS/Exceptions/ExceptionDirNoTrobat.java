package Exceptions;

public class ExceptionDirNoTrobat extends Exception{
    public ExceptionDirNoTrobat(String path){
        super("No s'ha trobat el directori: " + path);
    }
}
