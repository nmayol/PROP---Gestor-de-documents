package Exceptions;

import utils.Format;

public class ExceptionNotPrimaryKeys extends Exception{
     public ExceptionNotPrimaryKeys(Format format, String Titol, String Autor) {
         super("Les claus primaries" + format + " " + Titol + " " + Autor + " no són valides");
     }

}
