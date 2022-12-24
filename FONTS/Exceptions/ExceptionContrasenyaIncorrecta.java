package Exceptions;

public class ExceptionContrasenyaIncorrecta extends Exception {

    public ExceptionContrasenyaIncorrecta(String nom){
        super("Contrasenya incorrecta per l'usuari " + nom);
    }
}
