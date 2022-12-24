package Exceptions;


import utils.Format;

public class ExceptionPasswordDiferents extends Exception {
    public ExceptionPasswordDiferents(String p1, String p2) {
        super("Les contrasenyes no coincideixen");
    }
}
