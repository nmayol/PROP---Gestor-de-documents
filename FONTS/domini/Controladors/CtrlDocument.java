package domini.Controladors;

import Exceptions.*;
import domini.*;
import utils.*;

import java.security.KeyStore;
import java.util.*;

public class CtrlDocument{
    private HashMap<KeyP, Document> Cjt_documents;
    private HashMap<String, PairP<Integer, Boolean>> ParaulaDoc;
    private IndexAutor index;
    private IndexDocumentsAutor indexDA;
    private IndexExpressionsBooleanes indexEB;


    //CONSTRUCTORA
    /**
     * Constructora per defecte
     */
    public CtrlDocument(){
        super();
        Cjt_documents = new HashMap<KeyP, Document>();
        ParaulaDoc = new HashMap<String, PairP<Integer, Boolean>>();
        this.index = new IndexAutor();
        this.indexDA = new IndexDocumentsAutor();
        this.indexEB = new IndexExpressionsBooleanes();
    }

    //FUNCIONS
    /**
     * Elimina el document que te com a Titol nom_document i com a Autor nom_autor
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @throws ExceptionDocumentNoExisteix Salta quan el document no existeix
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix
     */
    public void EliminarDocument(String Autor, String Titol) throws ExceptionDocumentNoExisteix, ExceptionAutorNoExisteix, ExceptionCampsBuits {
        Document doc = getDocument(Autor, Titol);
        if(doc == null) throw new ExceptionDocumentNoExisteix(Autor, Titol);
        else {
            KeyP clau = new KeyP(Autor, Titol);
            Cjt_documents.remove(clau);
        }
        actualitzar_docs(doc, "Eliminar");
        indexDA.eliminarTitolAutor(Autor, Titol);
    }

    /**
     * Crea un document que te com a Titol nom_document i com a Autor nom_autor
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @param format Format del document
     * @param contingut Contingut del document
     * @throws ExceptionDocumentJaExisteix Salta quan el document ja existeix
     * @throws ExceptionNotPrimaryKeys Salta quan les claus primaries passades com a parametres no són valides
     * @throws ExceptionFormatNoValid Salta quan el format passat com a parametre no és valid
     */
    public void NouDocument(Format format, String Autor, String Titol, ArrayList<String> contingut) throws ExceptionDocumentJaExisteix, ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        if (Cjt_documents.containsKey(new KeyP(Autor, Titol))) throw new ExceptionDocumentJaExisteix(Autor, Titol);
        Document d;
        d = new Document(format, Titol, Autor, contingut);
        add_document(d);

        index.afegirAutor_sempre(Autor);
        indexDA.afegeixTitolAutor(Autor, Titol);

        indexEB.evaluarTotesExpressions(d);
    }


    /**
     * Modifica el document que te com a Titol nom_document i com a Autor nom_autor
     * @param Autor_antic Autor del document
     * @param Titol_antic Titol del document
     * @param Autor_nou Nou Autor del document
     * @param Titol_nou Nou Titol del document
     * @param Contingut_nou Nou contingut del document
     * @throws ExceptionDocumentNoExisteix Salta quan el document no existeix
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix
     * @throws ExceptionDocumentJaExisteix Salta quan ja existeix un document amb les claus primaries especificades
     * @throws ExceptionNotPrimaryKeys Salta quan les claus primaries passades com a parametres no són valides
     * @throws ExceptionFormatNoValid Salta quan el format passat com a parametre no és valid
     */
    public void ModificarInformacioDocument(String Autor_antic, String Titol_antic, String Autor_nou, String Titol_nou, ArrayList<String> Contingut_nou) throws ExceptionDocumentNoExisteix, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDocumentJaExisteix, ExceptionFormatNoValid, ExceptionNotPrimaryKeys, ExceptionCampsBuits {
        if ((Autor_antic != Autor_nou  | Titol_antic != Titol_nou) && Cjt_documents.get(new KeyP(Autor_nou, Titol_nou)) != null) throw new ExceptionDocumentJaExisteix(Autor_nou, Titol_nou);
        else{
            Document doc = getDocument(Autor_antic, Titol_antic);

            indexEB.esborratDocument(doc);

            doc.ModificarInformacioDocument(Titol_nou, Autor_nou, Contingut_nou);

            EliminarDocument(Autor_antic, Titol_antic);
            add_document(doc);

            indexEB.evaluarTotesExpressions(doc);
            index.afegirAutor_sempre(Autor_nou);
            indexDA.afegeixTitolAutor(Autor_nou, Titol_nou);
        }
    }


    //SETTERS
    /**
     * Afageix el document passat com a parametre al Conjunt de Documents
     * @param d Document a afegir
     */
    public void add_document(Document d){
        KeyP clau = new KeyP(d.getAutor(),d.getTitol());
        Cjt_documents.put(clau, d);
        actualitzar_docs(d, "Afegir");
    }
    /**
     * Actuzalitza el nombre de documents on apareix cada paraula
     * @param d Document a afegir
     * @param Accio Accio que s'ha realitzat (afegir o eliminar)
     */
    public void actualitzar_docs(Document d, String Accio){
        for (String word : ParaulaDoc.keySet()){
            int nou_pes = ParaulaDoc.get(word).getFirst();
            PairP<Integer, Boolean> p = new PairP<Integer, Boolean>(nou_pes, false);
            ParaulaDoc.replace(word, p);
        }

        if (Accio == "Afegir"){
            for (int i = 0; i < d.getContingut().size(); ++i){
                if (!ParaulaDoc.containsKey(d.getContingut().get(i))) {
                    PairP<Integer, Boolean> p = new PairP<Integer, Boolean>(1, true);
                    ParaulaDoc.put(d.getContingut().get(i), p);
                }
                else {
                    if (ParaulaDoc.get(d.getContingut().get(i)).getSecond() == false){
                        int pes = ParaulaDoc.get(d.getContingut().get(i)).getFirst();
                        ++pes;
                        PairP<Integer, Boolean> p = new PairP<Integer, Boolean>(pes, true);
                        ParaulaDoc.replace(d.getContingut().get(i), p);
                    }
                }
            }
        }
        else if (Accio == "Eliminar"){
            for (int i = 0; i < d.getContingut().size(); ++i){
                if (ParaulaDoc.containsKey(d.getContingut().get(i))) {
                    if (ParaulaDoc.get(d.getContingut().get(i)).getSecond() == false){
                        if (ParaulaDoc.get(d.getContingut().get(i)).getFirst() == 1) ParaulaDoc.remove(d.getContingut().get(i));
                        else{
                            int pes = ParaulaDoc.get(d.getContingut().get(i)).getFirst();
                            --pes;
                            PairP<Integer, Boolean> p = new PairP<Integer, Boolean>(pes, true);
                            ParaulaDoc.replace(d.getContingut().get(i), p);
                        }
                    }
                }
            }
        }
    }

    /**
     * Llista els K documents mes semblants al document que té com a titol Titol i com a autor Autor
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @param num_documents nombre de documents a llistar
     * @throws ExceptionDocumentNoExisteix Salta quan el document no existeix
     * @throws ExceptionNoEnterValid Salta quan l'enter especificat és menor a 0
     */
    public ArrayList<String[]> LlistarDocumentsSemblants_pesos(String Autor, String Titol, final int num_documents) throws ExceptionDocumentNoExisteix, ExceptionNoEnterValid, ExceptionCampsBuits {
        HashMap<Document, Double> docs_semblants = new HashMap<Document, Double>();

        if (Titol == null | Autor == null) throw new ExceptionDocumentNoExisteix(" ", " ");
        Document d = getDocument(Autor, Titol);

        if (!Cjt_documents.containsKey(new KeyP(Autor, Titol))) throw new ExceptionDocumentNoExisteix(Autor, Titol);
        if (num_documents < 0) throw new ExceptionNoEnterValid(num_documents);

        for(Map.Entry entry : Cjt_documents.entrySet()){
            Document doc = (Document) entry.getValue();
            if (doc != d) {
                double q_d= 0.0;
                double q_doc = 0.0;
                double q_final = 0.0;
                double q_norma = 0.0;
                double d_norma = 0.0;

                for (Map.Entry<String, Double> s : d.getPesosLocals().entrySet()){
                    double d_tf;
                    double q_tf = s.getValue();
                    double idf = Math.log10((double)Cjt_documents.size() / ParaulaDoc.get(s.getKey()).getFirst());
                    if (doc.getPesosLocals().containsKey(s.getKey())) d_tf = doc.getPesosLocals().get(s.getKey());
                    else d_tf = 0;

                    q_d = q_tf*idf;
                    q_doc = d_tf*idf;
                    q_final += q_d*q_doc;
                    q_norma += q_d*q_d;
                    d_norma += q_doc*q_doc;
                }

                double denominador = Math.sqrt(q_norma*d_norma);

                if (denominador != 0) docs_semblants.put(doc, q_final/denominador);
                else docs_semblants.put(doc, 0.0);
            }
        }
        final Map<Document, Double> sorted = SortedValue.sortByValue(docs_semblants);

        ArrayList<String[]> docs = new ArrayList<String[]>();

        int k = 1;
        for (Map.Entry<Document, Double> entry : sorted.entrySet()){
            if (k > num_documents) break;
            else {
                docs.add(new String[]{entry.getKey().getTitol(), entry.getKey().getAutor(), String.valueOf(entry.getKey().getFormat())});
                ++k;
            }
        }
        return docs;
    }

    /**
     * Llista els K documents mes semblants al document que té com a titol Titol i com a autor Autor
     * @param Autor Autor del document
     * @param Titol Titol del document
     * @param num_documents nombre de documents a llistar
     * @throws ExceptionDocumentNoExisteix Salta quan el document no existeix
     * @throws ExceptionNoEnterValid Salta quan l'enter especificat és menor a 0
     */
    public ArrayList<String[]> LlistarDocumentsSemblants_bool(String Autor, String Titol, final int num_documents) throws ExceptionDocumentNoExisteix, ExceptionNoEnterValid, ExceptionCampsBuits {
        HashMap<Document, Double> docs_semblants = new HashMap<Document, Double>();

        Document d = getDocument(Autor, Titol);

        if (!Cjt_documents.containsKey(new KeyP(Autor, Titol))) throw new ExceptionDocumentNoExisteix(Autor, Titol);
        if (num_documents < 0) throw new ExceptionNoEnterValid(num_documents);

        ArrayList<String> contingut_doc_general = d.getContingut();

        for(Map.Entry entry : Cjt_documents.entrySet()){
            Double paraules_iguals = 0.0;
            Document doc = (Document) entry.getValue();
            ArrayList<String> contingut_doc_nou = doc.getContingut();
            if (doc != d) {
                for(int i = 0; i < contingut_doc_general.size(); ++i){
                    if (contingut_doc_nou.contains(contingut_doc_general.get(i))) ++paraules_iguals;
                    docs_semblants.put(doc, paraules_iguals);
                }
            }
        }
        final Map<Document, Double> sorted = SortedValue.sortByValue(docs_semblants);

        ArrayList<String[]> docs = new ArrayList<String[]>();

        int k = 1;
        for (Map.Entry<Document, Double> entry : sorted.entrySet()){
            if (k > num_documents) break;
            else {
                docs.add(new String[]{entry.getKey().getTitol(), entry.getKey().getAutor(), String.valueOf(entry.getKey().getFormat())});
                ++k;
            }
        }

        return docs;
    }

    /**
     * Llista tots els documents de l'autor Autor
     * @param Autor Autor del document
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix
     */
    public ArrayList<String[]> LlistarDocumentsAutor(String Autor) throws ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String[]> docs = new ArrayList<>();

        if (index.cercarAutors(Autor) == null) throw new ExceptionAutorNoExisteix(Autor);
        Set<String> titols = indexDA.buscarTitolsAutor(Autor);
        Iterator it = titols.iterator();
        while (it.hasNext()){
            String s = (String) it.next();
            Document d = Cjt_documents.get(new KeyP(Autor, s));
            docs.add(new String[]{d.getTitol(), d.getAutor(), String.valueOf(d.getFormat())});
        }
        return docs;
    }

    /**
     * Llista els K documents mes semblants a les paraules clau
     * @param query conjunt de paraules clau amb les que comparar
     * @param k nombre de documents a llistar
     * @throws ExceptionDocumentNoExisteix Salta quan el document no existeix
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix
     * @throws ExceptionNoEnterValid Salta quan l'enter especificat és menor a 0
     * @throws ExceptionFormatNoValid Salta quan el format especificat no és valid
     * @throws ExceptionNotPrimaryKeys Salta quan les claus primaries no són valides
     */

    public ArrayList<String[]> Llistar_query(ArrayList<String> query, final int k) throws ExceptionDocumentNoExisteix, ExceptionAutorNoExisteix, ExceptionDocumentJaExisteix, ExceptionNoEnterValid, ExceptionFormatNoValid, ExceptionNotPrimaryKeys, ExceptionCampsBuits {
        if (k < 0) throw new ExceptionNoEnterValid(k);
        NouDocument(Format.txt, "query", "query", query);
        ArrayList<String[]> docs = LlistarDocumentsSemblants_bool("query", "query", k);
        EliminarDocument("query", "query");
        index.eliminarAutor("query");
        return docs;
    }

    /**
     * Llista tots els autors que contenen el prefix especificat
     * @param prefix prefix amb el que filtrar
     * @throws ExceptionPrefixNoExisteix Salta quan el prefix no existeix
     */
    public ArrayList<String> LlistarAutorPrefix(String prefix) throws ExceptionPrefixNoExisteix {
        return index.cercarAutors(prefix);
    }

    /**
     * Llista tots els documents que compleixen la expressió booleana
     * @param expressio expressio amb la que filtrar
     * @throws ExceptionExpressioBooleanaIncorrecta Salta quan l'expressió no és valida
     */
    public ArrayList<String[]> LlistarExpressioBooleana(String expressio) throws ExceptionExpressioBooleanaIncorrecta {

        for(Map.Entry entry : Cjt_documents.entrySet()){
            indexEB.evaluarTotesExpressions((Document) entry.getValue());
        }
        ArrayList<String[]> res = indexEB.getDocumentsExpressio(expressio);


        return res;
    }


    /**
     * Retorna les dades dels documents ordenades alfabeticament
     * @param docs llistat de dades a ordenar
     */
    public ArrayList<String[]> ordenar_alfabeticament(ArrayList<String[]> docs){
        Collections.sort(docs, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        return docs;
    }
    /**
     * Retorna les dades dels documents ordenades per datd de modificacio
     * @param docs llistat de dades a ordenar
     */
    public ArrayList<String[]> ordenar_data(ArrayList<String[]> docs){
        Collections.sort(docs, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                if (Cjt_documents.get(new KeyP(o1[1], o1[0])).getData().isAfter(Cjt_documents.get(new KeyP(o2[1], o2[0])).getData())) return -1;
                return 1;
            }
        });
        return docs;
    }

    /**
     * Esborra tots els documents del conjunt
     */
    public void eliminar_documents(){
        Cjt_documents.clear();
    }

    /**
     * Retorna l'index d'autors
     */
    public IndexAutor getIndexAutor() {
        return index;
    }

    /**
     * Estableix com a valor index a l'atribut index
     * @param index valor a assignar
     */
    public void setIndexAutor(IndexAutor index) {
        this.index = index;
    }
    /**
     * Retorna l'index documents autor
     */
    public IndexDocumentsAutor getIndexDA() {
        return indexDA;
    }

    /**
     * Estableix com a valor indexDA a l'atribut index documents autor
     * @param indexDA valor a assignar
     */
    public void setIndexDA(IndexDocumentsAutor indexDA) {
        this.indexDA = indexDA;
    }

    /**
     * Retorna l'index d'expressions booleanes
     */
    public IndexExpressionsBooleanes getIndexEB() {
        return indexEB;
    }

    /**
     * Estableix com a valor indexDA a l'atribut index expressio booleana
     * @param indexEB valor a assignar
     */
    public void setIndexEB(IndexExpressionsBooleanes indexEB) {
        this.indexEB = indexEB;
    }

    /**
     * Retorna les dades dels documents ordenades segons el criteri especificat
     * @param docs llistat de dades a ordenar
     * @param criteri criteri a aplicar en l'ordenació
     */
    public ArrayList<String[]> ordenar_docs(ArrayList<String[]> docs, String criteri) {
        if (criteri.equals("modificacio")) {
            return ordenar_data(docs);
        }
        else {
            return ordenar_alfabeticament(docs);
        }
    }

    //GETTERS
    /**
     * Retorna el document que té com a titol Titol i com a autor Autor
     * @param Autor Autor del document
     * @param Titol Titol del document
     */
    public Document getDocument (String Autor, String Titol) throws ExceptionDocumentNoExisteix, ExceptionCampsBuits {
        if (Autor.equals("") && Titol.equals("")) throw new ExceptionCampsBuits();
        else if (!Cjt_documents.containsKey(new KeyP(Autor,Titol))) throw new ExceptionDocumentNoExisteix(Titol, Autor);
        else return Cjt_documents.get(new KeyP(Autor,Titol));
    }
    /**
     * Retorna el conjunt de documents
     */
    public ArrayList<Document> getCjt_documents(){
        ArrayList<Document> docs = new ArrayList<>();
        for (Map.Entry<KeyP, Document> s : Cjt_documents.entrySet())  docs.add(s.getValue());
        return docs;
    }

    public ArrayList<String> getContingutDocument(String autor,String titol) {
        return Cjt_documents.get(new KeyP(autor,titol)).getContingut();
    }

    public static void netejarIndexEB() {
        IndexExpressionsBooleanes.netejarIndex();
    }

    public void novaEB(String expr) throws ExceptionExpressioBooleanaIncorrecta {
        indexEB.afegirExpressio(expr);
    }


    public ArrayList<String> getContingutIndexEB() {
        return IndexExpressionsBooleanes.getContingutIndex();
    }


}