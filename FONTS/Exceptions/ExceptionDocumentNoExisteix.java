package Exceptions;

public class ExceptionDocumentNoExisteix extends Exception {
    public ExceptionDocumentNoExisteix(String Autor, String Titol) {
            super("El document " + Titol + " --- " + Autor + " no existeix");
    }
}
