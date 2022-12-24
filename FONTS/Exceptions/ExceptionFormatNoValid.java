package Exceptions;

import utils.Format;

public class ExceptionFormatNoValid extends Exception {

    public ExceptionFormatNoValid(Format format) {
        super("Format " + format + " no valid");
    }
}
