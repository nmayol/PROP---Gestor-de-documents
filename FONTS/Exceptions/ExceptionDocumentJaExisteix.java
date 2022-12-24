package Exceptions;

public class ExceptionDocumentJaExisteix extends Exception {

    public ExceptionDocumentJaExisteix(String autor, String titol) {
        super("Document " + titol + " de " + autor + " ja existeix");
    }
}
