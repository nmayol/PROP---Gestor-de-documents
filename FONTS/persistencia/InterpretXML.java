package persistencia;




import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe per gestionar canvis de format entre documents, ja sigui quan es vol carregar un document i es
 * necessita passar de format XML al llenguatge que accepta el gestor o viceversa, a l'hora de recuperar el document.
 * @author Neus Mayol
 */
public class InterpretXML extends Interpret{
    public InterpretXML() {
        super();
    }

    /**
     * Agafa el contingut del fitxer XML que es vol crear i es posa de tal manera que el puguem afegir a una instancia
     * de la classe document del nostre gestor.
     * @param f El fitxer XML que es vol carregar.
     */
    @Override
    public void TradueixCarrega(File f) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory=SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        MyXMLHandler parser = new MyXMLHandler();
        saxParser.parse(f,parser);
        Titol = parser.getT();
        Autor = parser.getA();
        Contingut = parser.getContent();
    }

    /**
     * Agafa la informació d'un document del gestor i la converteix al contingut d'un fitxer del format XML.
     * @param t Titol del document
     * @param a Autor del document
     * @param Content Contingut del document
     */
    @Override
    public String TradueixRecupera(String t, String a, ArrayList<String> Content) {
        String listString = String.join(" ", Content);
        //listString = listString.replace(" $$BREAK$$ ","\n    ").replace(" $$BREAK$$","\n");;
        return "<document>" + "\n" + "  <autor>" + a  + "</autor>" + "\n" + "  <titol>" + t + "</titol>" + "\n" + "    <contingut>" + listString + "</contingut>" + "\n" + "</document>";
    }
}
