package Exceptions;

public class ExceptionCarregaDuplicada extends Exception {

        public ExceptionCarregaDuplicada(){
            super("No pots carregar dos documents amb el mateix títol i autor");
        }


}
