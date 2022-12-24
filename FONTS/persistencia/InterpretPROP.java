package persistencia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe per gestionar canvis de format entre documents, ja sigui quan es vol carregar un document i es
 * necessita passar de format PROP al llenguatge que accepta el gestor o viceversa, a l'hora de recuperar el document.
 * @author Neus Mayol
 */
public class InterpretPROP extends Interpret {

    public InterpretPROP() {
        super();
    }

    /**
     * Agafa el contingut del fitxer PROP que es vol crear i es posa de tal manera que el puguem afegir a una instancia
     * de la classe document del nostre gestor.
     * @param f El fitxer PROP que es vol carregar.
     */
    @Override
    public void TradueixCarrega(File f) throws FileNotFoundException {
        String ruta = f.getAbsolutePath();
        Scanner myReader = new Scanner(f);
        String rawContent = "";

        Contingut = new ArrayList<>();
        while (myReader.hasNext()) {
            rawContent += myReader.next();
        }

        rawContent = rawContent.replaceAll(";"+ Pattern.quote(")"), Matcher.quoteReplacement(":)\n:)"));
        ArrayList<String> AllContent = new ArrayList<>(Arrays.asList(rawContent.split(":"+Pattern.quote(")"))));

        boolean titol = false, autor = false; int t = 0;
        for (int i = 0; !autor && i < AllContent.size(); ++i) {
            if (AllContent.get(i).equals("\n")) {
                if (!titol) {
                    titol = true;
                    t = i;
                    List<String> subArrayTitol = AllContent.subList(0, t);
                    Titol = new String(String.join(" ", subArrayTitol));
                } else {
                    autor = true;
                    List<String> subArrayAutor = AllContent.subList(t + 1, i);
                    Autor = new String(String.join(" ", subArrayAutor));
                    List<String> subArrayContingut = AllContent.subList(i + 1, AllContent.size() - 1);
                    Contingut.addAll(new ArrayList<String>(subArrayContingut));
                }
            }

        }
    }

    /**
     * Agafa la informació d'un document del gestor i la converteix al contingut d'un fitxer del format PROP.
     * @param t Titol del document
     * @param a Autor del document
     * @param Content Contingut del document
     */
    @Override
    public String TradueixRecupera(String t, String a, ArrayList<String> Content) {
        String listString = String.join(":)", Content);
        listString = listString.replace(":)\n:)",";)").replace(":)\n",";)").replace("\n:)",";)");
        return a.replaceAll(" ",":)") + ";)" + t.replaceAll(" ",":)") + ";)" + listString;

    }
}
