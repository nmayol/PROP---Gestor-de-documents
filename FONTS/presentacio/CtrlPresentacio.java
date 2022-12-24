package presentacio;

import Exceptions.*;
import domini.Controladors.CtrlDomini;

import org.xml.sax.SAXException;
import utils.Format;
import utils.KeyP;
import utils.PairP;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controladora de la capa de presentacio
 */
public class CtrlPresentacio {

    private static CtrlPresentacio singletonObject;
    private final CtrlDomini cDomini;

    // Vistes

    private PantallaCarregarDocument vPantallaCarregarDocument;
    private PantallaDadesAutors vPantallaDadesAutor;
    private PantallaDadesEliminar vPantallaDadesEliminar;
    private PantallaDadesModificar vPantallaDadesModificar;
    private PantallaDadesObrir vPantallaDadesObrir;
    private PantallaDadesPClau vPantallaDadesPClau;
    private PantallaDadesPrefix vPantallaDadesPrefix;
    private PantallaDadesSemblants vPantallaDadesSemblants;
    private PantallaEliminarUsuaris vPantallaEliminarUsuaris;
    private PantallaLlistar vPantallaLlistar;
    private PantallaIniciarSessio vPantallaIniciarSessio;
    private PantallaLlistaAutorsPrefix vPantallaLlistaAutorsPrefix;
    private PantallaNovesDades vPantallaNovesDades;
    private PantallaPrincipal vPantallaPrincipal;
    private PantallaRecuperarDocument vPantallaRecuperarDocument;
    private PantallaRegistrarUsuari vPantallaRegistrarUsuari;
    private PantallaObrirDocument vPantallaObrirDocument;
    private PantallaLlistarExpressioBooleana vPantallaLlistarExpressioBooleana;

    // variables per eliminar usuari
    private String usuari;
    private String contrasenya;

    private String TITOL_ANTIC;
    private String AUTOR_ANTIC;
    private Format FORMAT_ANTIC;
    private ArrayList<String> CONTINGUT_ANTIC;

    private String TITOL_NOU;

    private String AUTOR_NOU;
    private Format FORMAT_NOU;
    private ArrayList<String> CONTINGUT_NOU;


    /**
     * Retorna el singleton de ctrlPresentacio i el crea en cas que sigui null
     * @return Retorna ctrlPresentacio
     */
    public static CtrlPresentacio getInstance() {
        if (singletonObject == null) {
            singletonObject = new CtrlPresentacio();
        }
        return singletonObject;
    }

    /**
     * Creadora per defecte del controlador
     */
    private CtrlPresentacio() {
        cDomini = new CtrlDomini();
    }

    /**
     * Inicialitza la capa de presentacio
     * @throws ExceptionDirectoriNoCreat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     */
    public void iniciPresentacio() throws ExceptionDirectoriNoCreat, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat {
        cDomini.obrirAplicacio();
        vPantallaIniciarSessio = new PantallaIniciarSessio();
        vPantallaIniciarSessio.setVisible(true);
    }

    //------------------------------USUARI------------------------------

    /**
     * Afegeix un usuari
     * @param usuari nom de l'usuari que volem crear
     * @param password contrasenya de l'usuari que volem crear
     * @throws ExceptionUsuariJaExisteix salta quan el nom de l'usuari ja existeix
     * @throws ExceptionParametresErronis salta quan els parametres introduits son erronis
     * @throws ExceptionDirectoriNoCreat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     */
    public void afegirUsuari(String usuari, String password) throws ExceptionUsuariJaExisteix, ExceptionParametresErronis, ExceptionDirectoriNoCreat, ExceptionDirNoTrobat, ExceptionIO {
        cDomini.crearUsuari(usuari, password);
    }

    /**
     * Elimina un usuari
     * @throws ExceptionUsuariNoExisteix salta quan l'usuari no existeix
     * @throws ExceptionContrasenyaIncorrecta salta quan la contrasenya es incorrecte
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     */
    public void eliminar_usuari() throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        cDomini.eliminarUsuari(usuari, contrasenya);
        usuari = null;
        contrasenya = null;
    }

    /**
     * Inicia sessio de l'usuari
     * @param usuari nom de l'usuari que volem iniciar sessio
     * @param password contrasenya de l'usuari que volem iniciar sessio
     * @throws ExceptionUsuariNoExisteix salta quan l'usuari no existeix
     * @throws ExceptionContrasenyaIncorrecta salta quan la contrasenya es incorrecte
     * @throws ExceptionSessioJaActiva salta quan ja hi ha una sessio activa
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan algun dels camps es buit
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     */
    public void iniciarSessio(String usuari, String password) throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta, ExceptionSessioJaActiva, ExceptionDocumentJaExisteix, ExceptionDirNoTrobat, ExceptionCampsBuits, ExceptionIO, ExceptionDirectoriNoEliminat {
        cDomini.iniciarSessio(usuari, password);
    }

    /**
     * Tanca la sessio
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     */
    public void tancarSessio() throws ExceptionNoSessioActiva, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        vPantallaPrincipal.dispose();
        controlaVista("PantallaIniciarSessio");
        cDomini.tancarSessio();
    }

    /**
     * Es guarda el nom de l'usuari a la capa de presentacio
     * @param nom nom de l'usuari que volem guardar
     */
    public void guardar_nom_usuari(String nom) {
        usuari = nom;
    }

    /**
     * Es guarda la contrasenya de l'usuari a la capa de presentacio
     * @param nom contrasenya de l'usuari que volem guardar
     */
    public void guardar_nom_contrasenya(String nom) {
        contrasenya = nom;
    }

    /**
     * Retorna la llista d'usuaris existents a l'aplicacio
     * @return Retorna la llista d'usuaris existents a l'aplicacio
     */
    public String[] llistar_tots_usuaris() {
        ArrayList<String[]> conjunt_usuaris = cDomini.getUsuaris();
        String[] noms_usuaris = new String[conjunt_usuaris.size()];
        for (int i = 0; i < conjunt_usuaris.size(); ++i) {
            noms_usuaris[i] = conjunt_usuaris.get(i)[0];
        }
        return noms_usuaris;
    }

    /**
     * Actualitza els usuaris de la Pantalla Eliminar Usuari
     */
    public void actualitzar_usuaris_PantallaEliminarUsuari() {
        vPantallaEliminarUsuaris.llistar_tots_usuaris();
    }

    //------------------------------DOCUMENT------------------------------

    /**
     * Carrega un conjunt de documents a l'aplicacio
     * @param titols titols dels documents que volem carregar
     * @param autors autors dels documents que volem carregar
     * @param formats formats dels documents que volem carregar
     * @param continguts continguts dels documents que volem carregar
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionParametresErronis salta quan els parametres son erronis
     * @throws ExceptionFormatNoValid salta quan algun format no es valid
     * @throws ExceptionDocumentJaExisteix salta quan un document ja existeix
     * @throws ExceptionNotPrimaryKeys salta quan l'identificador del document es erroni
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     */
    public void carregarDocuments(ArrayList<String> titols, ArrayList<String> autors, ArrayList<Format> formats, ArrayList<ArrayList<String>> continguts)
            throws ExceptionNoSessioActiva, ExceptionParametresErronis, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        cDomini.carregarDocuments(autors, titols,continguts, formats, titols.size());
    }

    /**
     * Elimina un document
     * @param Autor autor del document que volem eliminar
     * @param Titol titol del document que volem eliminar
     * @throws ExceptionDocumentNoExisteix salta quan un document no existeix
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionAutorNoExisteix salta quan l'autor no existeix
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan algun dels camps esta buit
     */
    public void eliminar_doc(String Autor, String Titol) throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionAutorNoExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        cDomini.eliminarDocument(Autor, Titol);
    }

    /**
     * Guarda les dades del document que volem modificar
     * @param Autor autor document antic
     * @param Titol titol document antic
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionConsultaLimitParametres salta quan una consulta no introdueix correctament el limit de parametres
     * @throws ExceptionCampsBuits salta quan algun parametre esta buit
     */
    public void getDadesDocumentAntic(String Autor, String Titol) throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionCampsBuits {
        ArrayList<String> antic = cDomini.getInfoDocument(Autor, Titol);
        TITOL_ANTIC = antic.get(0);
        AUTOR_ANTIC = antic.get(1);
        CONTINGUT_ANTIC = cDomini.getContingutDocument(Autor, Titol);
    }

    /**
     * Actualitza el document amb les noves dades
     * @param Autor autor document nou
     * @param Titol titol document nou
     * @param Contingut contingut document nou
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionFormatNoValid salta quan el format no es valid
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionDocumentJaExisteix salta quan el document ja existeix
     * @throws ExceptionNotPrimaryKeys salta quan l'identificador del document es erroni
     * @throws ExceptionAutorNoExisteix salta quan l'autor antic no existeix
     * @throws ExceptionAutorJaExisteix salta quan l'autor nou ja existeix
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan algun parametre esta buit
     */
    public void getDadesDocumentNou(String Autor, String Titol, String Contingut) throws ExceptionDocumentNoExisteix, ExceptionFormatNoValid, ExceptionNoSessioActiva, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        AUTOR_NOU = Autor;
        TITOL_NOU = Titol;
        if (!Contingut.isEmpty()){
            CONTINGUT_NOU = new ArrayList<String>(Arrays.asList(Contingut.split(" ")));
        }
        else CONTINGUT_NOU = new ArrayList<String>();
        modificar_document();
    }

    /**
     * Modifica el document
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionFormatNoValid salta quan el format no es valid
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionDocumentJaExisteix salta quan les noves dades son d'un document que ja existeix
     * @throws ExceptionNotPrimaryKeys salta quan l'identificador del document es erroni
     * @throws ExceptionAutorNoExisteix salta quan l'autor antic no existeix
     * @throws ExceptionAutorJaExisteix salta quan l'autor nou ja existeix
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan algun dels camps es buit
     */
    private void modificar_document() throws ExceptionDocumentNoExisteix, ExceptionFormatNoValid, ExceptionNoSessioActiva, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if (TITOL_NOU.isBlank()) TITOL_NOU = TITOL_ANTIC;
        if (AUTOR_NOU.isBlank()) AUTOR_NOU = AUTOR_ANTIC;
        if (CONTINGUT_NOU.size()==0) {
            CONTINGUT_NOU = CONTINGUT_ANTIC;
        }
        cDomini.modificarDocument(AUTOR_ANTIC, TITOL_ANTIC, AUTOR_NOU, TITOL_NOU, CONTINGUT_NOU);
        TITOL_NOU = null;
        AUTOR_NOU = null;
        CONTINGUT_NOU = new ArrayList<>();
        TITOL_ANTIC = null;
        AUTOR_ANTIC = null;
        CONTINGUT_ANTIC = new ArrayList<>();
        actualitzar_docs();
    }

    /**
     * Retorna el contingut d'un document
     * @param autor autor del document
     * @param titol titol del document
     * @return retorna contingut del document
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionCampsBuits salta quan algun dels camps esta buit
     */
    public ArrayList<String> getContingutDocument(String autor, String titol) throws ExceptionNoSessioActiva, ExceptionDocumentNoExisteix, ExceptionCampsBuits {
        return cDomini.getContingutDocument(autor,titol);
    }

    /**
     * comprova si existeix un document
     * @param autor autor del document
     * @param titol titol del document
     * @return retorna cert si existeix o fals en cas contrari
     * @throws ExceptionNoSessioActiva salta quan hi ha una sessio activa
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionCampsBuits salta quan algun dels camps esta buit
     */
    public boolean existeix_document (String autor, String titol) throws ExceptionNoSessioActiva, ExceptionDocumentNoExisteix, ExceptionCampsBuits {
        return cDomini.existeixDocument(titol, autor);
    }


    /**
     * Canvia la pantalla que es visible a l'aplicacio
     * @param tipus Inidica quina pantalla volem fer visible
     */
    public void controlaVista(String tipus) {
        if (tipus.equals("PantallaCarregarDocument")) {
            vPantallaCarregarDocument = new PantallaCarregarDocument();
            vPantallaCarregarDocument.setVisible(true);
            actualitzar_docs();
        }
        else if (tipus.equals("PantallaDadesAutors")) {
            try {
                vPantallaDadesAutor = new PantallaDadesAutors();
            } catch (ExceptionNoSessioActiva e) {
                popup(e.getMessage(), "Error");
            }
            vPantallaDadesAutor.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesEliminar")) {
            vPantallaDadesEliminar = new PantallaDadesEliminar();
            vPantallaDadesEliminar.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesModificar")) {
            vPantallaDadesModificar = new PantallaDadesModificar();
            vPantallaDadesModificar.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesObrir")) {
            vPantallaDadesObrir = new PantallaDadesObrir();
            vPantallaDadesObrir.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesPClau")) {
            vPantallaDadesPClau = new PantallaDadesPClau();
            vPantallaDadesPClau.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesPrefix")) {
            vPantallaDadesPrefix = new PantallaDadesPrefix();
            vPantallaDadesPrefix.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesSemblants")) {
            vPantallaDadesSemblants = new PantallaDadesSemblants();
            vPantallaDadesSemblants.setVisible(true);
        }
        else if (tipus.equals("PantallaEliminarUsuaris")) {
            vPantallaEliminarUsuaris = new PantallaEliminarUsuaris();
            vPantallaEliminarUsuaris.setVisible(true);
        }
        else if (tipus.equals("PantallaIniciarSessio")) {
            vPantallaIniciarSessio = new PantallaIniciarSessio();
            vPantallaIniciarSessio.setVisible(true);
        }

        else if (tipus.equals("PantallaNovesDades")) {
            vPantallaNovesDades = new PantallaNovesDades();
            vPantallaNovesDades.setVisible(true);
        }
        else if (tipus.equals("PantallaPrincipal")) {
            vPantallaIniciarSessio.dispose();
            vPantallaPrincipal = new PantallaPrincipal(cDomini.getDocumentsString());
            vPantallaPrincipal.setVisible(true);
        }
        else if (tipus.equals("PantallaRecuperarDocument")) {
            String[] dades = vPantallaPrincipal.getFilaSeleccionada();
            try {
                setDefaultValues("PantallaRecuperaDocument", dades[1], dades[0]);
            } catch (ExceptionNoSessioActiva e) {
                popup(e.getMessage(), "Error");
            }
        }
        else if (tipus.equals("PantallaRegistrarUsuari")) {
            vPantallaIniciarSessio.dispose();
            vPantallaRegistrarUsuari = new PantallaRegistrarUsuari();
            vPantallaRegistrarUsuari.setVisible(true);
        }
        else if (tipus.equals("PantallaLlistarExpressioBooleana")) {
            vPantallaLlistarExpressioBooleana = new PantallaLlistarExpressioBooleana();
            vPantallaLlistarExpressioBooleana.setVisible(true);
        }
        else {
            popup("La pantalla que es vol obrir no existeix", "Error");
        }
    }

    /**
     * Canvia la pantalla que es visible a l'aplicacio i li passem unes dades per llistar a la pantalla
     * @param tipus indica la pantalla que volem fer visible
     * @param Autor autor que volem llistar
     * @param Titol titol que volem llistar
     */
    public void setDefaultValues(String tipus, String Autor, String Titol) throws ExceptionNoSessioActiva {
        if (tipus.equals("PantallaDadesEliminar")) {
            vPantallaDadesEliminar = new PantallaDadesEliminar(Autor, Titol);
            vPantallaDadesEliminar.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesObrir")) {
            vPantallaDadesObrir = new PantallaDadesObrir(Autor, Titol);
            vPantallaDadesObrir.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesModificar")) {
            vPantallaDadesModificar = new PantallaDadesModificar(Autor, Titol);
            vPantallaDadesModificar.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesAutors")) {
            vPantallaDadesAutor = new PantallaDadesAutors(Autor);
            vPantallaDadesAutor.setVisible(true);
        }
        else if (tipus.equals("PantallaDadesSemblants")) {
            vPantallaDadesSemblants = new PantallaDadesSemblants(Autor, Titol);
            vPantallaDadesSemblants.setVisible(true);
        }
        else if (tipus.equals("PantallaRecuperaDocument")) {
            vPantallaRecuperarDocument = new PantallaRecuperarDocument(Autor, Titol);
            vPantallaRecuperarDocument.setVisible(true);
        }
    }

    /**
     * Fa visible la pantalla recuperar document
     * @param autor autor document
     * @param titol titol document
     */
    public void RecuperaDocument(String autor, String titol) {
        vPantallaRecuperarDocument = new PantallaRecuperarDocument(autor,titol);
        vPantallaRecuperarDocument.setVisible(true);
    }

    /**
     * Fa visible la pantalla obrir document
     * @param autor autor document
     * @param titol titol document
     */
    public void ObreDocument(String autor, String titol) {
        vPantallaObrirDocument = new PantallaObrirDocument(autor,titol);
        vPantallaObrirDocument.setVisible(true);
    }

    /**
     * Ordena els documents a llistar
     * @param docs_a_llistar Llista de docs que volem llistar
     * @param criteri criteri que volem fer servir per ordenar
     * @return Retorna els documents ordenats
     */
    public ArrayList<String[]> ordenar_docs(ArrayList<String[]> docs_a_llistar, String criteri) {
        return cDomini.ordenar_docs(docs_a_llistar, criteri);
    }

    /**
     * Tanca l'aplicacio
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     */
    public void tancarAplicacio() throws ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        cDomini.tancarAplicacio();
    }

    /**
     * Actualitza els documents de la pantalla principal
     */
    public void actualitzar_docs() {
        vPantallaPrincipal.initTable(cDomini.getDocumentsString());
    }

    // pop up confirmacio
    /**
     * Pop-up per confirmar una accio
     * @param missatge missatge de l'accio que volem confirmar
     * @return Retorna cert si volem confirmar l'accio i fals en cas contrari
     */
    public Boolean accepta_popup(String missatge) {
        Object[] options = { "OK", "CANCEL" };
        int opcio = JOptionPane.showOptionDialog(null,
                missatge, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return opcio == 0;
    }

    /**
     * Pop-up per demanar unes dades a l'usuari
     * @param missatge missatge per demanar les dades
     * @return retorna cert si volem demanar les dades i fals en cas contrari
     */
    public Boolean demana_dades(String missatge) {
        Object[] options = { "OK", "CANCEL" };
        int opcio = JOptionPane.showOptionDialog(null,
                missatge, "Contrasenya usuari",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return opcio == 0;
    }

    // pop up error i informacio
    /**
     * Pop-up per informar d'una accio realitzada o d'una excepcio
     * @param missatge missatge informatiu
     * @param tipus tipus de missatge
     */
    public void popup(String missatge, String tipus) {
        Object[] options = { "OK", "CANCEL" };
        if (tipus.equals("Error")) { // quan salten excepcions
            JOptionPane.showInternalMessageDialog(null, missatge,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (tipus.equals("Informacio")) {
            JOptionPane.showInternalMessageDialog(null, missatge,
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Fa visible la pantalla llistar documents d'un autor
     * @param nom_autor autor a partir del qual es llista
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     * @throws ExceptionAutorNoExisteix salta quan l'autor no existeix
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionPrefixNoExisteix salta quan cap autor conte el prefix
     */
    public void llistarDocumentsAutor(String nom_autor) throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix {
        ArrayList<String[]> documents = cDomini.llistarDocumentsAutor(nom_autor);
        vPantallaLlistar = new PantallaLlistar(documents, "AUTOR");
        vPantallaLlistar.setVisible(true);
        vPantallaIniciarSessio.setVisible(false);
        vPantallaPrincipal.dispose();
    }

    /**
     * Fa visible la pantalla llistar autor d'un prefix
     * @param prefix prefix a partir del qual es llista
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     * @throws ExceptionAutorNoExisteix salta quan l'autor no existeix
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionPrefixNoExisteix salta quan cap usuari no conte el prefix
     */
    public void llistarAutorsPerPrefix(String prefix) throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix {
        ArrayList<String> autors_segons_prefix = cDomini.llistarAutorsPrefix(prefix);
        if (autors_segons_prefix.size() != 0) vPantallaPrincipal.dispose();
        vPantallaLlistaAutorsPrefix = new PantallaLlistaAutorsPrefix(autors_segons_prefix);
        vPantallaLlistaAutorsPrefix.setVisible(true);
    }

    /**
     * Fa visible la pantalla de llistar segons les paraules clau
     * @param paraules paraules clau segons les que es llista
     * @param num_docs num de documents segons paraules clau que volem llistar
     * @throws ExceptionDocumentNoExisteix salta quan algun document no existeix
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionFormatNoValid salta quan algun format no es valid
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     * @throws ExceptionNotPrimaryKeys salta quan l'indentificacio del document es incorrecte
     * @throws ExceptionAutorNoExisteix salta quan l'autor no existeix
     * @throws ExceptionAutorJaExisteix salta quan l'autor ja existeix
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan hi ha camps buits
     */
    public void llistarDocumentsPClau(String paraules, int num_docs) throws ExceptionNoEnterValid, ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionConsultaLimitParametres, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(paraules.split(" ")));
        ArrayList<String[]> documents = cDomini.llistarDocumentsPC(myList, num_docs);
        vPantallaLlistar = new PantallaLlistar(documents, "PARAULES CLAU");
        vPantallaLlistar.setVisible(true);
        vPantallaPrincipal.setVisible(false);
    }

    /**
     * Fa visible la pantalla llistar documents semblants
     * @param Autor autor
     * @param Titol titol
     * @param K numero de documents a llistar
     * @param criteri_semblansa criteri de semblança
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     * @throws ExceptionAutorNoExisteix salta quan l'autor no existeix
     * @throws ExceptionDocumentNoExisteix salta quan el document no existeix
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionCampsBuits salta quan hi ha camps buits
     */
    public void llistarDocumentsSemblants(String Autor, String Titol, Integer K, String criteri_semblansa) throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionNoEnterValid, ExceptionDocumentNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        ArrayList<String[]> documents = cDomini.llistarDocumentsKSemblants(Autor, Titol, K, criteri_semblansa);
        vPantallaLlistar = new PantallaLlistar(documents, "SEMBLANTS");
        vPantallaLlistar.setVisible(true);
        vPantallaPrincipal.setVisible(false);
    }

    /**
     * Tradueix el document carregat
     * @param f fitxer que volem traduir
     * @return Retorna el fitxer traduit
     * @throws IOException salta quan hi ha un problema amb la capa de persistencia
     * @throws ParserConfigurationException salta quan hi ha un problema traduint el fitxer
     * @throws SAXException salta quan hi ha un problema traduint el fitxer
     */
    public PairP<KeyP, PairP<Format,ArrayList<String>>>tradueixCarrega(File f) throws IOException, ParserConfigurationException, SAXException {
        return cDomini.tradueixCarrega(f);
    }

    /**
     * Recupera el document
     * @param Path path on volem guardar
     * @param Autor autor doc
     * @param Titol titol doc
     * @param Contingut contingut doc
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws IOException salta quan hi ha un problema amb la capa de persistencia
     */
    public void GuardaDocument(String Path, String Autor, String Titol, ArrayList<String> Contingut) throws ExceptionDirNoTrobat, IOException {
        cDomini.recuperarDocument(Path,Autor,Titol,Contingut);
    }

    /**
     * Buida historial expressio booleana
     * @throws ExceptionNoSessioActiva salta quan no hi ha cap sessio activa
     */
    public void netejarIndexEB() throws ExceptionNoSessioActiva {
        cDomini.netejarIndexEB();
    }

    /**
     * Retorna l'index d'expressions booleanes
     * @return Retorna l'index d'expressions booleanes
     * @throws ExceptionExpressioBooleanaIncorrecta salta quan l'expressio booleana es incorrecte
     * @throws ExceptionNoSessioActiva salta quan no hi ha cap sessio activa
     */
    public ArrayList<String> getIndexExpressions() throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva {
        return cDomini.getExpressions();
    }

    /**
     * Retorna els documents que compleixen una expressio booleana
     * @param expr expressio booleana a partir de la qual volem llistar
     * @return Retorna documents
     * @throws ExceptionExpressioBooleanaIncorrecta salta quan l'expressio booleana es incorrecte
     * @throws ExceptionNoSessioActiva salta quan no hi ha una sessio activa
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     * @throws ExceptionDirNoTrobat salta quan hi ha un problema amb la capa de persistencia
     * @throws ExceptionIO salta quan hi ha un problema amb la capa de persistencia
     */
    public ArrayList<String[]> getDocumentsExpressio(String expr) throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO {
        return cDomini.llistarDocumentsExpressio(expr);
    }

    /**
     * Crea nova consulta expressio booleana
     * @param s expressio booleana que volem crear
     * @throws ExceptionExpressioBooleanaIncorrecta salta quan l'expressio booleana es incorrecte
     * @throws ExceptionNoSessioActiva salta quan no hi ha cap sessio activa
     */
    public void novaEB(String s) throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva {
        cDomini.novaExpressio(s);
    }

    public String[] getAutorsHistorial() throws ExceptionNoSessioActiva {
        return cDomini.getAutorsHistorial();
    }
    public String[] getPrefixosHistorial() throws ExceptionNoSessioActiva {
        return cDomini.getPrefixosHistorial();
    }

    public String[] getParaulesHistorial() throws ExceptionNoSessioActiva {
        return cDomini.getParaulesHistorial();
    }

    public ArrayList<String[]> getSemblantsHistorial() throws ExceptionNoSessioActiva {
        return cDomini.getSemblantsHistorial();
    }
}