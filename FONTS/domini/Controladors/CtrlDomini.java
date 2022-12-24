package domini.Controladors;

import Exceptions.*;
import domini.*;
import org.xml.sax.SAXException;
import persistencia.CtrlPersistencia;
import persistencia.accesObjects.*;
import utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CtrlDomini {

    private final CtrlConsulta ctrlConsultes;
    private final CtrlDocument ctrlDocument;
    private final CtrlUsuari ctrlUsuari;
    private final CtrlPersistencia ctrlPersistencia;

    private final ConvertirAO convertidorAO;

    /** Constructora.
     *
     */
    public CtrlDomini() {
        this.ctrlConsultes = new CtrlConsulta();
        this.ctrlDocument = new CtrlDocument();
        this.ctrlUsuari = new CtrlUsuari();
        this.ctrlPersistencia = new CtrlPersistencia();
        this.convertidorAO = new ConvertirAO();
    }

    // ----------------------------APLICACIo----------------------------------------------------------------------------
    /** Crida a les operacions de persistencia necessaries per carregar/crear les dades.
     *
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear algun dels directoris.
     * @see CtrlUsuari#setUsuaris(ArrayList)
     * @see CtrlPersistencia
     */
    public void obrirAplicacio()
            throws ExceptionDirectoriNoCreat, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat {
        ctrlPersistencia.obrirAplicacio();
        ArrayList<Usuari> usuaris = new ArrayList<>();
        ctrlPersistencia.carregarUsuaris().forEach(s -> usuaris.add(new Usuari(s[0], s[1])));
        ctrlUsuari.setUsuaris(usuaris);
        ctrlDocument.setIndexAutor(null);
        ctrlDocument.setIndexDA(null);
        ctrlDocument.setIndexEB(null);
    }

    /** Tanca la sessio si hi ha alguna oberta.
     *
     */
    public void tancarAplicacio()
            throws ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        try {
            tancarSessio();
        } catch (ExceptionNoSessioActiva ignored){}
    }

    // ----------------------------USUARI-------------------------------------------------------------------------------

    /** Retorna el conjunt de tots els usuaris de l'aplicacio
     *
     * @return Representacions d'usuari.
     * @see CtrlUsuari#getUsuaris()
     */
    public ArrayList<String[]> getUsuaris(){
        ArrayList<String[]> usuaris = new ArrayList<>();
        ctrlUsuari.getUsuaris().forEach(u -> usuaris.add(convertirUsuari(u)));
        return usuaris;
    }

    /** Crea un usuari amb el nom i la contrasenya aportats.
     *
     * @param nom Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @throws ExceptionUsuariJaExisteix Ja existeix un usuari amb el nom aportat.
     * @throws ExceptionParametresErronis No es pot crear un usuari amb contrasenya nul·la.
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear un directori.
     * @see CtrlUsuari#afegirUsuari(Usuari)
     * @see CtrlPersistencia#nouUsuari(String, String)
     */
    public void crearUsuari(String nom, String contrasenya)
            throws ExceptionUsuariJaExisteix, ExceptionParametresErronis, ExceptionDirectoriNoCreat, ExceptionDirNoTrobat, ExceptionIO {
        if(nom.isBlank()) throw new ExceptionParametresErronis("Crear usuari: nom");
        if(contrasenya.isBlank()) throw new ExceptionParametresErronis("Crear usuari: contrasenya");
        ctrlUsuari.afegirUsuari(new Usuari(nom, contrasenya));

        ctrlPersistencia.nouUsuari(nom, contrasenya);
    }

    /** Elimina un usuari de l'aplicacio i disc.
     *
     * @param nom Nom de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     * @throws ExceptionUsuariNoExisteix No existeix l'usuari.
     * @throws ExceptionContrasenyaIncorrecta La contrasenya no es correcta per l'usuari a eliminar.
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar l'usuari o algunes de les seves carpetes de disc.
     * @see CtrlUsuari#eliminarUsuari(String, String)
     * @see CtrlPersistencia#eliminarUsuari(String)
     */
    public void eliminarUsuari(String nom, String contrasenya)
            throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        ctrlUsuari.eliminarUsuari(nom, contrasenya);
        ctrlPersistencia.eliminarUsuari(nom);
    }

    /** Obre sessio per a un usuari. Executa les funcions necessaries de persistencia.
     * Carrega les consultes, documents, i indexs. Si no hi ha indexs els crea.
     *
     * @param nom Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari.
     * @throws ExceptionUsuariNoExisteix No existeix l'usuari amb el nom aportat.
     * @throws ExceptionSessioJaActiva No hi ha cap usuari amb sessio.
     * @throws ExceptionContrasenyaIncorrecta La contrasenya aportada no es correcta.
     * @see CtrlUsuari
     * @see CtrlPersistencia
     * @see CtrlConsulta
     */
    public void iniciarSessio(String nom, String contrasenya)
            throws ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionDocumentJaExisteix, ExceptionIO, ExceptionCampsBuits, ExceptionDirNoTrobat, ExceptionDirectoriNoEliminat {
        if(nom.isBlank() && contrasenya.isBlank()) throw new ExceptionCampsBuits();

        try {
            ctrlPersistencia.iniciarSessio(nom);
        } catch (ExceptionDirNoTrobat e) {
            throw new ExceptionUsuariNoExisteix(nom);
        }

        ArrayList<ConsultaAO> consultesAO = ctrlPersistencia.carregarConsultesUsuari(nom);
        ArrayList<Consulta> consultes = new ArrayList<>();
        for (ConsultaAO consultaAO : consultesAO) {
            try {
                consultes.add(convertidorAO.convertirConsultaAO(consultaAO));
            } catch (ExceptionConsultaLimitParametres e) {
                throw new ExceptionIO("S'ha trobat una consulta a disc amb format incorrecte");
            }
        }
        ctrlConsultes.setConsultes(consultes);
        consultes.forEach(System.out::println);

        IndexAutorAO ia_AO = null;
        try {
            ia_AO = ctrlPersistencia.carregarIndexAutorUsuari(nom);
            if(ia_AO != null) ctrlDocument.setIndexAutor(convertidorAO.convertirIA_AO(ia_AO));
            else ctrlDocument.setIndexAutor(new IndexAutor());
        } catch (ExceptionDirNoTrobat e) {
            ctrlDocument.setIndexAutor(new IndexAutor());
        }

        IndexDocumentsAutorAO ida_AO = null;
        try {
            ida_AO = ctrlPersistencia.carregarIndexDocumentsAutorUsuari(nom);
            if (ida_AO != null) ctrlDocument.setIndexDA(convertidorAO.convertirIDA_AO(ida_AO));
            else ctrlDocument.setIndexDA(new IndexDocumentsAutor());
        } catch (ExceptionDirNoTrobat e) {
            ctrlDocument.setIndexDA(new IndexDocumentsAutor());
        }

        IndexExpressionsBooleanesAO ieb_AO = null;
        try {
            ieb_AO = (ctrlPersistencia.carregarIndexExpressionsBooleanesUsuari(nom));
            if(ieb_AO != null) ctrlDocument.setIndexEB(convertidorAO.convertirIEB_AO(ieb_AO));
            else ctrlDocument.setIndexEB(new IndexExpressionsBooleanes());
        } catch (ExceptionDirNoTrobat e) {
            ctrlDocument.setIndexEB(new IndexExpressionsBooleanes());
        }

        ArrayList<DocumentAO> documents = ctrlPersistencia.carregarDocumentsUsuari(nom);
        for (DocumentAO d: documents) {
            if(d != null) {
                try {
                    ctrlDocument.add_document(convertidorAO.convertirDocAO(d));
                } catch (ExceptionFormatNoValid | ExceptionNotPrimaryKeys e) {
                    throw new ExceptionIO("S'ha trobat un document a disc amb format incorrecte");
                }
            }
        }

        ctrlUsuari.iniciarSessio(nom, contrasenya);
    }

    /** Tanca la sessio activa. Esborra totes les dates de la sessio. Executa les funcions de persistencia necessaries.
     *
     * @throws ExceptionNoSessioActiva No hi ha cap usuari amb sessio.
     * @see CtrlUsuari#tancarSessio()
     * @see CtrlPersistencia
     * @see CtrlConsulta#EsborrarConsultes()
     * @see CtrlDocument#eliminar_documents()
     */
    public void tancarSessio()
            throws ExceptionNoSessioActiva, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        Usuari usuariActiu = ctrlUsuari.getUsuariActiu();
        if (usuariActiu == null) throw new ExceptionNoSessioActiva();
        ctrlConsultes.EsborrarConsultes();
        ctrlDocument.eliminar_documents();
        ctrlDocument.setIndexAutor(null);
        ctrlDocument.setIndexDA(null);
        ctrlDocument.setIndexEB(null);
        ctrlUsuari.tancarSessio();
        ctrlPersistencia.tancarSessio(usuariActiu.getNom());
    }

//    /**
//     *
//     * @return
//     */
//    public ArrayList<String[]> getManual(){
//        ArrayList<Document> docs = ctrlDocument.getCjt_documents();
//        ctrlPersistencia.get
//        ArrayList<String[]> docsString = new ArrayList<>();
//        docs.forEach(document -> docsString.add(convertirdoc(document)));
//        return docsString;
//    }

    // ----------------------------CONSULTA-----------------------------------------------------------------------------

    /** Elimina la k-esima ultima consulta de l'historial del tipus especificat.
     *
     * @param tipusConsulta Tipus d'historial.
     * @param k Ordre.
     * @throws ExceptionIndexConsultaNoValid No existeix la k-esima consulta, no hi ha suficients.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @see CtrlConsulta#esborrarConsulta(TipusConsulta, int)
     */
    public void eliminarConsulta(TipusConsulta tipusConsulta, int k)
            throws ExceptionIndexConsultaNoValid, ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ctrlConsultes.esborrarConsulta(tipusConsulta, k);
    }

    private void guardarConsulta(Consulta c) throws ExceptionDirNoTrobat, ExceptionIO {
        ctrlConsultes.NovaConsulta(c);
        ArrayList<ConsultaAO> cAO = new ArrayList<>();
        ctrlConsultes.getHistorialTipus(c.getTipus()).forEach(consulta -> cAO.add(convertidorAO.convertirConsulta(consulta)));
        ctrlPersistencia.guardarConsultesUsuari(ctrlUsuari.getUsuariActiu().getNom(), cAO, c.getTipus());
    }

    /** Retorna l'historial de documents d'autor.
     *
     * @return Llista de les consultes com a expressio del historial.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @see CtrlConsulta
     */
    public String[] getAutorsHistorial() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<Consulta> consultes = ctrlConsultes.getHistorialTipus(TipusConsulta.docsAutor);
        String[] llista = new String[consultes.size()];
        int i = 0;
        for (Consulta c: consultes){
            llista[consultes.size()-1-i] = c.getParametres().get(0);
            ++i;
            if (i == 5) break;
        }
        return llista;
    }

    /** Retorna l'historial d'autors segons prefix.
     *
     * @return Llista de les consultes com a expressio del historial.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @see CtrlConsulta
     */
    public String[] getPrefixosHistorial() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<Consulta> consultes = ctrlConsultes.getHistorialTipus(TipusConsulta.autorsPrefix);
        String[] llista = new String[consultes.size()];
        int i = 0;
        for (Consulta c: consultes){
            llista[i] = c.getParametres().get(0);
            ++i;
            if (i == 5) break;
        }
        return llista;
    }

    /** Retorna l'historial de paraules clau.
     *
     * @return Llista de les consultes com a expressio del historial.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @see CtrlConsulta
     */
    public String[] getParaulesHistorial() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<Consulta> consultes = ctrlConsultes.getHistorialTipus(TipusConsulta.docsPC);
        String[] llista = new String[consultes.size()];
        int i = 0;
        for (Consulta c: consultes){
            String s = new String();
            for (int j = 0; j < c.getParametres().size() - 1; ++j){
                s += c.getParametres().get(j);
                if (j != c.getParametres().size() - 2) s += " ";
            }
            llista[i] = s;
            ++i;
            if (i == 5) break;
        }
        return llista;
    }

    /** Retorna l'historial de documents semblants.
     *
     * @return Llista de les consultes com a expressio del historial.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @see CtrlConsulta
     */
    public ArrayList<String[]> getSemblantsHistorial() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<Consulta> consultes = ctrlConsultes.getHistorialTipus(TipusConsulta.docsKSemblants);
        ArrayList<String[]> llista =  new ArrayList<String[]>(consultes.size());
        int i = 0;
        for (Consulta c: consultes){
            String[] dades = new String[]{c.getParametres().get(0), c.getParametres().get(1)};
            llista.add(i, dades);
            ++i;
        }
        return llista;
    }

    /** Retorna els parametres de la k-esima ultima consulta de l'historial del tipus especificat.
     *
     * @param tipusConsulta Tipus d'historial.
     * @param k Ordre.
     * @throws ExceptionIndexConsultaNoValid No existeix la k-esima consulta, no hi ha suficients.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @return Llistat amb els parametres de la consulta.
     * @see CtrlConsulta
     */
    public String[] getParametresConsulta(TipusConsulta tipusConsulta, int k) throws ExceptionIndexConsultaNoValid, ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        return ctrlConsultes.getConsulta(tipusConsulta, k).getParametres().toArray(new String[0]);
    }

    // ----------------------------DOCUMENT-----------------------------------------------------------------------------

    /** Comprova si existeix un document. No es comprova al disc, nomes als documents carregats en l'actual sessio.
     *
     * @param titol Titol del document
     * @param autor Autor del document
     * @return True si el document existeix a l'aplicacio.
     */
    public boolean existeixDocument(String titol, String autor) throws ExceptionNoSessioActiva, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        try {
            ctrlDocument.getDocument(autor, titol);
        } catch (ExceptionDocumentNoExisteix e) {
            return false;
        }
        return true;
    }
    /** Retorna la informacio de tots els documents. No inclou el contingut.
     *
     * @return Conjunt de String[], cada entrada es la informacio de un document.
     * @see CtrlDocument#getCjt_documents()
     */
    public ArrayList<String[]> getDocumentsString(){
        ArrayList<Document> docs = ctrlDocument.getCjt_documents();
        ArrayList<String[]> docsString = new ArrayList<>();
        docs.forEach(document -> docsString.add(convertirdoc(document)));
        return docsString;
    }

    /** Ordena i retorna un conjunt de documents segons un criteri aportat.
     *
     * @param docs Conjunt de documents.
     * @param criteri Criteri per ordenar.
     * @return Conjunt de representacions String[] de documents. No inclou el contingut.
     * @see CtrlDocument#ordenar_docs(ArrayList, String)
     */
    public ArrayList<String[]> ordenar_docs(ArrayList<String[]> docs, String criteri){
        return ctrlDocument.ordenar_docs(docs, criteri);
    }

    /** Elimina un document de la aplicacio. No persistencia.
     *
     * @param nom_autor Autor del document a eliminar.
     * @param nom_document Titol del document a eliminar.
     * @throws ExceptionDocumentNoExisteix No existeix el document especificat.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @throws ExceptionAutorNoExisteix No existeix l'autor especificat.
     * @see CtrlDocument#EliminarDocument(String, String)
     */
    public void eliminarDocument(String nom_autor, String nom_document)
            throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionAutorNoExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ctrlDocument.EliminarDocument(nom_autor, nom_document);
        ctrlPersistencia.eliminarDocumentUsuari(ctrlUsuari.getUsuariActiu().getNom(), nom_document, nom_autor);
        guardarIndexs();
    }

    /** Canvia la informacio o el contingut d'un document especificat per l'usuari.
     *
     * @param nom_autor Autor actual del document.
     * @param nom_document Titol actual del document
     * @param nou_autor Nou autor del document.
     * @param nou_nom Nou titol del document.
     * @param nou_contingut Nou contingut del document.
     * @throws ExceptionDocumentNoExisteix No existeix el document especificat.
     * @throws ExceptionFormatNoValid El format aportat no es correcte.
     * @throws ExceptionDocumentJaExisteix Ja existeix un altre document amb les noves dades.
     * @throws ExceptionAutorNoExisteix No existeix l'autor especificat.
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @throws ExceptionAutorJaExisteix Ja existeix un autor. Error intern.
     * @see CtrlDocument#ModificarInformacioDocument(String, String, String, String, ArrayList)
     */
    public void modificarDocument(String nom_autor, String nom_document, String nou_autor, String nou_nom, ArrayList<String> nou_contingut)
            throws ExceptionDocumentNoExisteix, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionNoSessioActiva, ExceptionAutorJaExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ctrlPersistencia.eliminarDocumentUsuari(ctrlUsuari.getUsuariActiu().getNom(), nom_document, nom_autor);
        ctrlDocument.ModificarInformacioDocument(nom_autor, nom_document, nou_autor, nou_nom, nou_contingut);
        DocumentAO documentAO = convertidorAO.convertirDoc(ctrlDocument.getDocument(nou_autor, nou_nom));
        ctrlPersistencia.guardarDocumentUsuari(ctrlUsuari.getUsuariActiu().getNom(), documentAO);
        guardarIndexs();
    }

    /** Es carrega un nou document a l'aplicacio.
     *
     * @param autor Autor del document
     * @param titol Titol del document
     * @param contingut Contingut del document
     * @param format Format del document
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio.
     * @throws ExceptionFormatNoValid El format aportat no es valid.
     * @throws ExceptionDocumentJaExisteix Ja existeix un document amb el nom i autor del document que es vol carregar.
     * @throws ExceptionNotPrimaryKeys Excepcio implementacio CtrlDocument
     * @see CtrlDocument#NouDocument(Format, String, String, ArrayList)
     */
    private void carregarDocument(String autor, String titol, ArrayList<String> contingut, Format format)
            throws ExceptionNoSessioActiva, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionDirNoTrobat, ExceptionIO {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        Document document = new Document(format, titol, autor, contingut);
        DocumentAO documentAO = convertidorAO.convertirDoc(document);
        ctrlDocument.NouDocument(format, autor, titol, contingut);
        ctrlPersistencia.guardarDocumentUsuari(ctrlUsuari.getUsuariActiu().getNom(), documentAO);
    }

    /** Per cada document de la llista es truca a carregar documents.
     *
     * @see CtrlDomini#carregarDocument(String, String, ArrayList, Format)
     * @throws ExceptionNoSessioActiva salta quan no hi ha cap sessio activa
     * @throws ExceptionFormatNoValid salta quan el format no es valid
     * @throws ExceptionDocumentJaExisteix salta quan el document ja existeix
     * @throws ExceptionNotPrimaryKeys salta quan les primary keys no son correctes
     * @throws ExceptionParametresErronis salta quan els parametres son erronis
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionDirectoriNoEliminat salta quan hi ha un error a persistencia
     */
    public void carregarDocuments(ArrayList<String> autors, ArrayList<String> titols, ArrayList<ArrayList<String>> continguts, ArrayList<Format> formats, int size)
            throws ExceptionNoSessioActiva, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionParametresErronis, ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        if(size != autors.size() | size != titols.size() | size != continguts.size() | size != formats.size()) throw new ExceptionParametresErronis("Carregar documents");
        for(int i = 0; i < size; i++){
            carregarDocument(autors.get(i), titols.get(i), continguts.get(i), formats.get(i));
        }
        guardarIndexs();
    }

    /** Guarda el fitxer corresponent al document especificat a la ruta especificada.
     *
     * @param Path Ruta
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @param Contingut Contingut del document
     */
    public void recuperarDocument(String Path, String Autor, String Titol, ArrayList<String> Contingut) throws ExceptionDirNoTrobat, IOException {
        ctrlPersistencia.recuperarDocument(Path,Autor,Titol,Contingut);
    }

    /** Obre un document especificat per l'autor i el titol aportats per l'usuari.
     *
     * @param nom_autor    Autor del document
     * @param nom_document Titol del document
     * @return Informacio de document, ordre: titol, autor, format
     * @throws ExceptionDocumentNoExisteix      No existeix el document especificat
     * @throws ExceptionNoSessioActiva          No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @see CtrlDocument#getDocument(String, String)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String> getInfoDocument(String nom_autor, String nom_document)
            throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();

        // Document
        Document doc = ctrlDocument.getDocument(nom_autor, nom_document);

        return new ArrayList<>(Arrays.asList(convertirdoc(doc)));
    }

    /** Retorna el contingut de un document
     *
     * @param nom_autor Autor del document
     * @param nom_document Titol del document
     * @return Contingut
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @see CtrlDocument#getContingutDocument(String, String)
     */
    public ArrayList<String> getContingutDocument(String nom_autor, String nom_document) throws ExceptionNoSessioActiva, ExceptionDocumentNoExisteix, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        if(!existeixDocument(nom_document, nom_autor)) throw new ExceptionDocumentNoExisteix(nom_autor, nom_document);
        return ctrlDocument.getContingutDocument(nom_autor, nom_document);
    }

    /** Retorna una llista de tots els autors que contenen el prefix
     *
     * @param prefix parametre
     * @return Conjunt de noms d'autor
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionPrefixNoExisteix No existeix el prefix
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @see CtrlDocument#LlistarAutorPrefix(String)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String> llistarAutorsPrefix(String prefix)
            throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<String> autors = ctrlDocument.LlistarAutorPrefix(prefix);
        ArrayList<String> parametres = new ArrayList<>();
        parametres.add(prefix);
        Consulta consulta = new Consulta(parametres, TipusConsulta.autorsPrefix);
        guardarConsulta(consulta);
        return autors;
    }

    /** Retorna una llista de documents segons una expressio booleana
     *
     * @param expressio parametre
     * @return Conjunt d'informacions(String[]) dels documents obtinguts
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionExpressioBooleanaIncorrecta La expressio aportada es incorrecta
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @see CtrlDocument#LlistarExpressioBooleana(String)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String[]> llistarDocumentsExpressio(String expressio)
            throws ExceptionNoSessioActiva, ExceptionExpressioBooleanaIncorrecta, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<String[]> docs = new ArrayList<>();
        docs = ctrlDocument.LlistarExpressioBooleana(expressio);
        return docs;
    }

    /** Retorna una llista de k documents semblants al document aportat.
     * @param nom_autor    Autor del document
     * @param nom_document Titol del document
     * @param k            parametre
     * @return Conjunt d'informacions(String[]) dels documents obtinguts
     * @throws ExceptionDocumentNoExisteix No existeix el document especificat
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionNoEnterValid Parematre incorrecte
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @see CtrlDocument#LlistarDocumentsSemblants_bool(String, String, int)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String[]> llistarDocumentsKSemblants(String nom_autor, String nom_document, int k, String criteri_semblansa)
            throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionNoEnterValid, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<String[]> docs = new ArrayList<>();
        if (criteri_semblansa.equals("bool")) docs = ctrlDocument.LlistarDocumentsSemblants_bool(nom_autor, nom_document, k);
        else docs = ctrlDocument.LlistarDocumentsSemblants_pesos(nom_autor, nom_document, k);
        ArrayList<String> parametres = new ArrayList<>();
        parametres.add(nom_autor);
        parametres.add(nom_document);
        parametres.add(Integer.toString(k));
        Consulta consulta = new Consulta(parametres, TipusConsulta.docsKSemblants);
        guardarConsulta(consulta);
        return docs;
    }

    /** Retorna una llista amb tots els documents d'un cert autor.
     *
     * @param nom_autor Autor del document
     * @return Conjunt d'informacions(String[]) dels documents obtinguts
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionAutorNoExisteix No existeix l'autor especificat
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @see CtrlDocument#LlistarDocumentsAutor(String)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String[]> llistarDocumentsAutor(String nom_autor)
            throws ExceptionNoSessioActiva, ExceptionAutorNoExisteix, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<String[]> docs = new ArrayList<>();
        docs = ctrlDocument.LlistarDocumentsAutor(nom_autor);
        ArrayList<String> parametres = new ArrayList<>();
        parametres.add(nom_autor);
        Consulta consulta = new Consulta(parametres, TipusConsulta.docsAutor);
        guardarConsulta(consulta);
        return docs;
    }

    /** Retorna una llista de documents segons certes paraules clau
     *
     * @param paraules_clau Parametres
     * @param numero_documents Numero de documents a retornar
     * @return Conjunt d'informacions(String[]) dels documents obtinguts
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @throws ExceptionConsultaLimitParametres Hi ha hagut un error en crear la consulta per l'historial
     * @throws ExceptionNoEnterValid parametre incorrecte
     * @throws ExceptionDocumentNoExisteix No existeix un cert document. Intern de CtrlDocument
     * @throws ExceptionFormatNoValid Error en el format d'un document. Intern de CtrlDocument
     * @throws ExceptionDocumentJaExisteix Intern de CtrlDocument
     * @throws ExceptionAutorNoExisteix No existeix l'autor especificat. Intern de CtrlDocument.
     * @throws ExceptionNotPrimaryKeys Intern de CtrlDocuments
     * @see CtrlDocument#Llistar_query(ArrayList, int)
     * @see CtrlConsulta#NovaConsulta(Consulta)
     */
    public ArrayList<String[]> llistarDocumentsPC(ArrayList<String> paraules_clau, int numero_documents)
            throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionNoEnterValid, ExceptionDocumentNoExisteix,
            ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionAutorNoExisteix, ExceptionNotPrimaryKeys, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ArrayList<String[]> docs = ctrlDocument.Llistar_query(paraules_clau, numero_documents);
        paraules_clau.add(Integer.toString(numero_documents));
        Consulta consulta = new Consulta(paraules_clau, TipusConsulta.docsPC);
        guardarConsulta(consulta);
        return docs;
    }

    /** Retorna totes les expressions booleanes utilitzades a l'aplicacio.
     *
     * @return Conjunt de expressions.
     * @see CtrlDocument#getContingutIndexEB()
     */
    public ArrayList<String> getExpressions() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        return ctrlDocument.getContingutIndexEB();
    }

    /** Elimina totes les expressions booleanes de l'aplicacio.
     *
     * @see CtrlDocument#netejarIndexEB()
     */
    public void netejarIndexEB() throws ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ctrlDocument.netejarIndexEB();
    }

    /** Afegeix una expressio booleana a l'aplicacio.
     *
     * @param expressio Nova expressio booleana
     * @throws ExceptionExpressioBooleanaIncorrecta La expressio aportada no es valida
     * @throws ExceptionNoSessioActiva No hi ha sessio activa, no es permet aquesta operacio
     * @see CtrlDocument#novaEB(String)
     */
    public void novaExpressio(String expressio) throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva {
        if(ctrlUsuari.getUsuariActiu() == null) throw new ExceptionNoSessioActiva();
        ctrlDocument.novaEB(expressio);
    }

    // ---------------------------INTERPRET----------------------------------------------------------
    public PairP<KeyP, PairP<Format,ArrayList<String>>> tradueixCarrega(File f) throws IOException, ParserConfigurationException, SAXException {
        return ctrlPersistencia.interpretarFitxer(f);
    }

    // -----------------------------private-----------------------------------------------------------------------------
    private String[] convertirdoc(Document d){
        return new String[]{d.getTitol(), d.getAutor(), String.valueOf(d.getFormat())};
    }

    private String[] convertirUsuari(Usuari u){
        return new String[]{u.getNom(), u.getContrasenya()};
    }

    private void guardarIndexs() throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        Usuari usuariActiu = ctrlUsuari.getUsuariActiu();
        IndexAutorAO ia_AO = convertidorAO.convertirIA(ctrlDocument.getIndexAutor());
        IndexDocumentsAutorAO ida_AO = convertidorAO.convertirIDA(ctrlDocument.getIndexDA());
        IndexExpressionsBooleanesAO ieb_AO = convertidorAO.convertirIEB(ctrlDocument.getIndexEB());
        ctrlPersistencia.guardarIndexsUsuari(usuariActiu.getNom(), ia_AO, ida_AO, ieb_AO);
    }
}
