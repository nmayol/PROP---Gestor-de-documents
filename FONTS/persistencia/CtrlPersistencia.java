package persistencia;

import Exceptions.*;
import org.xml.sax.SAXException;
import persistencia.accesObjects.*;
import utils.Format;
import utils.KeyP;
import utils.PairP;
import utils.TipusConsulta;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Controlador de totes les operacions de persistencia. Tots els accessos a disc es controlen des d'aquesta classe.
 * Aquesta classe nomes treballa amb access objects, que sn representacions serialitzables de les classes de domini.
 */
public class CtrlPersistencia {
    private final ConsultaPersistencia consultaP;
    private final UsuariPersistencia usuariP;
    private final DocumentPersistencia documentP;
    private final IndexPersistencia indexP;
    private final IOFitxers ioFitxers;

    /** Constructora. Inicialitza les instancies de totes les classes de persistencia: Usuari, Document, Consulta i indexs.
     * Inicialitza la classe IOFitxers que s'encarrega de fer els accessos a disc.
     *
     */
    public CtrlPersistencia() {
        usuariP = new UsuariPersistencia("EXE/persistencia/usuaris");
        consultaP = new ConsultaPersistencia("EXE/persistencia/usuaris");
        documentP = new DocumentPersistencia("EXE/persistencia/documents");
        indexP = new IndexPersistencia("EXE/persistencia/usuaris");
        ioFitxers = new IOFitxers();
    }

    /** Crea els directoris de persistencia si no existeixen.
     *
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear algun directori
     * @see IOFitxers#crearDirectori(String) 
     */
    public void obrirAplicacio() throws ExceptionDirectoriNoCreat {
        ioFitxers.crearDirectori("EXE/persistencia");
        ioFitxers.crearDirectori("EXE/persistencia/documents");
        ioFitxers.crearDirectori("EXE/persistencia/usuaris");
    }

    /** Log d'inici de sessi.
     *
     * @param user Nom d'usuari
     * @see UsuariPersistencia#iniciarSessio(String) 
     */
    public void iniciarSessio(String user) throws ExceptionDirNoTrobat, ExceptionIO {
        usuariP.iniciarSessio(user);
    }

    /** Log de tancar sessi.
     *
     * @param user Nom d'usuari
     * @see UsuariPersistencia#iniciarSessio(String)
     */
    public void tancarSessio(String user) throws ExceptionDirNoTrobat, ExceptionIO {
        usuariP.tancarSessio(user);
    }


    //-----------------------CONSULTA-----------------------------------------------------------------------------------

    /** Guarda una consulta en el directori de l'usuari especificat.
     *
     * @param usuari Nom de l'usuari.
     * @param consultesAO Consulta access object
     * @see ConsultaPersistencia#guardarConsulta(String, ConsultaAO, int)
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb persistencia
     */
    public void guardarConsultesUsuari(String usuari, ArrayList<ConsultaAO> consultesAO, TipusConsulta tipusConsulta) throws ExceptionDirNoTrobat, ExceptionIO {
        int i = 0;
        for (ConsultaAO c: consultesAO){
            if(c.getTipusConsulta() == tipusConsulta) consultaP.guardarConsulta(usuari, c, i++);
        }
    }

    /** Retorna totes les consultes de l'usuari especificat.
     *
     * @param nom Nom de l'usuari.
     * @return Llista de consultes (access objects)
     * @see ConsultaPersistencia#getConsultes(String)
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb persistencia
     */
    public ArrayList<ConsultaAO> carregarConsultesUsuari(String nom) throws ExceptionDirNoTrobat, ExceptionIO {
        return consultaP.getConsultes(nom);
    }

    //-----------------------DOCUMENT-----------------------------------------------------------------------------------

    /** Guarda un document per a l'usuari especificat. Si el document ja existia al directori de documents el sobreescriu.
     *
     * @param usuari Nom de l'usuari
     * @param documentAO Document a guardar (access object)
     * @see DocumentPersistencia#guardarDocument(DocumentAO) 
     * @see UsuariPersistencia#guardarDoc(String, String, String)
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb persistencia
     */
    public void guardarDocumentUsuari(String usuari, DocumentAO documentAO) throws ExceptionDirNoTrobat, ExceptionIO {
        documentP.guardarDocument(documentAO);
        usuariP.guardarDoc(usuari, documentAO.getTitol(), documentAO.getAutor());
    }

    /** Elimina un document per a l'usuari especificat. Si cap altre usuari necessita aquest document, s'elimina del directori de documents.
     *
     * @param usuari Nom de l'usuari
     * @param titol Titol del document
     * @param autor Autor del document
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar el document del directori de documents
     * @see UsuariPersistencia#eliminarDoc(String, String, String)
     * @see #updateDocuments()
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb persistencia
     */
    public void eliminarDocumentUsuari(String usuari, String titol, String autor) throws ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        usuariP.eliminarDoc(usuari, titol, autor);
        updateDocuments();
    }

    /** Retorna tots els documents d'un usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @return Llista de documents (access object)
     * @see UsuariPersistencia#getLlistaDocsUsuari(String) 
     * @see DocumentPersistencia#getDocument(String, String)
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb persistencia
     */
    public ArrayList<DocumentAO> carregarDocumentsUsuari(String nom) throws ExceptionDirNoTrobat, ExceptionIO {
        ArrayList<PairP<String, String>> llistaDocs = usuariP.getLlistaDocsUsuari(nom);
        ArrayList<DocumentAO> docs = new ArrayList<>();
        for (PairP<String, String> p: llistaDocs){
            docs.add(documentP.getDocument(p.getFirst(), p.getSecond()));
        }
        return docs;
    }

    /** Actualitza el directori de documents. Si hi ha algun document guardat que no es necessiti per part de cap usuari l'elimina.
     *
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar algun document
     */
    private void updateDocuments() throws ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        ArrayList<DocumentAO> docs = documentP.getDocuments();
        for (DocumentAO d: docs) {
            if (!usuariP.docUtilitzat(d.getTitol(), d.getAutor())) {
                documentP.eliminarDocument(d.getTitol(), d.getAutor());
            }
        }
    }

    //-----------------------USUARI-------------------------------------------------------------------------------------

    /** Es guarda el nom i contrasenya de l'usuari.
     * Si l'usuari no estava ja guardat, es crea la seva estructura de directoris de persistencia.
     * 
     * @param nom Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear un cert directori
     * @throws ExceptionUsuariJaExisteix Ja hi ha un usuari guardat amb el nom aportat
     * @see UsuariPersistencia#nouUsuari(String, String)
     */
    public void nouUsuari(String nom, String contrasenya) throws ExceptionDirectoriNoCreat, ExceptionUsuariJaExisteix, ExceptionDirNoTrobat, ExceptionIO {
        usuariP.nouUsuari(nom, contrasenya);
    }

    /** Elimina tots els directoris de l'usuari especificat. Actualitza la carpeta de documents.
     *
     * @param nom Nom de l'usuari.
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar un directori.
     * @see UsuariPersistencia#eliminarUsuari(String)
     * @see #updateDocuments()
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionUsuariNoExisteix salta quan un usuari no existeix
     */
    public void eliminarUsuari(String nom) throws ExceptionDirectoriNoEliminat, ExceptionUsuariNoExisteix, ExceptionDirNoTrobat, ExceptionIO {
        usuariP.eliminarUsuari(nom);
        updateDocuments();
    }

    /** Retorna la llista d'usuaris guardats a disc.
     *
     * @return Llista de conjunts (nom, contrasenya)
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     */
    public ArrayList<String[]> carregarUsuaris() throws ExceptionDirNoTrobat, ExceptionDirectoriNoEliminat {
        ArrayList<String[]> usuaris = new ArrayList<>();
        String path = "EXE/persistencia/usuaris";
        File dir = new File(path);
        if(dir.exists()){
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f: files){
                    File info = new File(f.getPath() + "/info.txt");
                    Scanner sc = null;
                    try {
                        sc = new Scanner(info);
                    } catch (FileNotFoundException e) {
                        throw new ExceptionDirNoTrobat(info.getPath());
                    }
                    String contrasenya = "";
                    String nom = "";
                    if(sc.hasNextLine()) contrasenya = sc.next();
                    if (sc.hasNextLine()) nom = sc.next();
                    if(!contrasenya.isBlank() && !nom.isBlank()) usuaris.add(new String[]{nom, contrasenya});
                    else {
                        ioFitxers.eliminarDirectori(f.getPath());
                    }
                }
            }
        }
        return usuaris;
    }
    //-----------------------INDEX--------------------------------------------------------------------------------------

    /** Guarda els tres indexs passats com a parametres per a l'usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @param ia Index autor access object
     * @param ida Index documents autor access object
     * @param ieb Index expressions booleanes access object
     * @see IndexPersistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     */
    public void guardarIndexsUsuari(String nom, IndexAutorAO ia, IndexDocumentsAutorAO ida, IndexExpressionsBooleanesAO ieb) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        indexP.guardarIndexAutor(nom, ia);
        indexP.guardarIndexDocumentsAutor(nom, ida);
        indexP.guardarIndexExpressionsBooleanes(nom, ieb);
    }

    /** Retorna l'index d'autors guardat a disc per a l'usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @return Index autor access object
     * @see IndexPersistencia#getIndexAutor(String) 
     */
    public IndexAutorAO carregarIndexAutorUsuari(String nom) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        return indexP.getIndexAutor(nom);
    }

    /** Retorna l'index de documents autor guardat a disc per a l'usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @return Index documents autor access object
     * @see IndexPersistencia#getIndexDocumentsAutor(String)
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     */
    public IndexDocumentsAutorAO carregarIndexDocumentsAutorUsuari(String nom) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        return indexP.getIndexDocumentsAutor(nom);
    }

    /** Retorna l'index d'expressions booleanes guardat a disc per a l'usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @return Index expressions booleanes access object
     * @see IndexPersistencia#getIndexExpressionsBooleanes(String)
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     */
    public IndexExpressionsBooleanesAO carregarIndexExpressionsBooleanesUsuari(String nom) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        return indexP.getIndexExpressionsBooleanes(nom);
    }

    // ----------------------- INTERPRET ---------------------------------- //
    /**
     * Funci que agafa el fitxer que es vol carregar, en transforma el seu contingut per fer-lo interpretable pel nostre gestor i n'extreu el titol i l'autor.
     * @param f el fitxer triat des de l'explorador de fitxers
     * @return Una estructura de dades amb el contingut del document deglossat i amb els valors preparats per ser assignats a les propietats del document.
     * @throws IOException Salta si hi ha un error de lectura del fitxer.
     * @throws ParserConfigurationException Error de configuraci del parser XML.
     * @throws SAXException Error del parser XML.
     */
    public PairP<KeyP, PairP<Format,ArrayList<String>>> interpretarFitxer(File f) throws IOException, ParserConfigurationException, SAXException {
        Interpret x = new InterpretTXT();
        KeyP clauPrimaria;
        PairP<Format,ArrayList<String>> propietats;
        Format format = Format.txt;

        String path = f.getAbsolutePath();
        if (path.endsWith("xml")) {
            x = new InterpretXML();
            format = Format.xml;
        } else if (path.endsWith("prop")){
            x = new InterpretPROP();
            format = Format.prop;
        }
        x.TradueixCarrega(f);

        clauPrimaria = new KeyP(x.getTitol(),x.getAutor());
        propietats = new PairP<>(format,x.getContingut());
        return (new PairP<>(clauPrimaria,propietats));
    }

    /** Guarda el fitxer corresponent al document especificat a la ruta especificada.
     *
     * @param path Ruta
     * @param autor Autor del document
     * @param titol Titol del document
     * @param contingut Contingut del document
     * @see IOFitxers#escriureFitxer(String, String, boolean)
     * @throws IOException salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     */
    public void recuperarDocument(String path, String autor, String titol, ArrayList<String> contingut) throws ExceptionDirNoTrobat, IOException {
        try {
            ioFitxers.escriureFitxer(path, tradueixDocumentInterpret(path,autor,titol,contingut),false);
        } catch (IOException e) {
            throw new IOException("No s'ha pogut escriure el fitxer " + path);
        }
    }

    /** Tradueix un document en el text del fitxer que es guarda en recuperar un document.
     *
     * @param Path Ruta
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @param Contingut Contingut del document
     * @return Contingut del fitxer.
     */
    private String tradueixDocumentInterpret(String Path, String Autor, String Titol, ArrayList<String> Contingut) {
        Interpret x = new InterpretTXT();
        if (Path.endsWith("xml")) {
            x = new InterpretXML();
        } else if (Path.endsWith("prop")){
            x = new InterpretPROP();
        }
        return x.TradueixRecupera(Titol,Autor,Contingut);
    }
}
