package Exceptions;

public class ExceptionPrefixNoExisteix extends Exception {
    public ExceptionPrefixNoExisteix(String pre) {
        super("No existeix cap autor amb el prefix " + pre);
    }
}
