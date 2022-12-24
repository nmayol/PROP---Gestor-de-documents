package persistencia;



import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe abstracta per gestionar canvis de format entre documents, ja sigui quan es vol carregar un document i es
 * necessita passar d'un dels tres formats al llenguatge que accepta el gestor o viceversa.
 * Els metodes
 * @author Neus Mayol
 */
public abstract class Interpret {

    protected String Titol;
    protected String Autor;
    protected ArrayList<String> Contingut;

    ////////////////////////////////////////////////////

    public Interpret() {
        super();
    }

/*
    /**
     * @return Getter amb el titol d'un fitxer que ha de tenir un document del gestor
     */
    public String getTitol() {
        return Titol;
    }

    /**
     * @return Getter amb l'autor d'un fitxer que ha de tenir un document del gestor
     */
    public String getAutor() {
        return Autor;
    }

    /**
     * @return Getter amb el contingut d'un fitxer que ha de tenir un document del gestor
     */
    public ArrayList<String> getContingut() {
        return Contingut;
    }

    /**
     * Agafa el contingut del fitxer que es vol crear i es posa de tal manera que el puguem afegir a una instancia
     * de la classe document del nostre gestor.
     *
     * @param f Fitxer que es vol carregar.
     */
    public abstract void TradueixCarrega(File f) throws IOException, ParserConfigurationException, SAXException;

    /**
     * Agafa la informació d'un document del gestor i la converteix al contingut d'un fitxer del format original.
     *
     * @param t       Titol del document
     * @param a       Autor del document
     * @param Content Contingut del document
     */
    public abstract String TradueixRecupera(String t, String a, ArrayList<String> Content);
}
