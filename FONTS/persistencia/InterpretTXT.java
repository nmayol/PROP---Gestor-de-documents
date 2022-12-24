package persistencia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Classe per gestionar canvis de format entre documents, ja sigui quan es vol carregar un document i es
 * necessita passar de format TXT al llenguatge que accepta el gestor o viceversa, a l'hora de recuperar el document.
 * @author Neus Mayol
 */
public class InterpretTXT extends Interpret{
    public InterpretTXT() {
        super();

    }

    /**
     * Agafa el contingut del fitxer TXT que es vol crear i es posa de tal manera que el puguem afegir a una instancia
     * de la classe document del nostre gestor.
     * @param f El fitxer TXT que es vol carregar.
     */
    @Override
    public void TradueixCarrega(File f) throws IOException {
        Scanner myReader = new Scanner(f);
        Autor = myReader.nextLine();
        Titol = myReader.nextLine();
        Contingut = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String linia = myReader.nextLine();
            if (!linia.equals("")) Contingut.addAll(new ArrayList<String>(Arrays.asList(linia.split(" "))));
            Contingut.add("\n");
        }
        myReader.close();
    }

    /**
     * Agafa la informació d'un document del gestor i la converteix al contingut d'un fitxer del format TXT.
     * @param t Titol del document
     * @param a Autor del document
     * @param Content Contingut del document
     */
    @Override
    public String TradueixRecupera(String t, String a, ArrayList<String> Content) {
        String listString = String.join(" ", Content);
        //listString = listString.replace(" $$BREAK$$ ","\n").replace(" $$BREAK$$","\n");
        return a + "\n" + t + "\n" + listString;
    }
}
