package Exceptions;

public class ExceptionExpressioBooleanaIncorrecta extends Exception {
    public ExceptionExpressioBooleanaIncorrecta(String expr) { super("L'espressio " + expr + " no es correcta"); }
}
