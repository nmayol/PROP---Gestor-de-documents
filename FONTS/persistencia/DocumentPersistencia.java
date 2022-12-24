package persistencia;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionIO;
import persistencia.accesObjects.DocumentAO;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/** Aquesta classe s'encarrega de fer els accessos a disc dels documents. Nomes treballa amb access objects.
 * @see DocumentAO
 */
public class DocumentPersistencia {

    private final IOFitxers ioFitxers;
    private final String path;

    /** Constructora. Inicialitza el path en el que treballa la classe i crea una instancia de IOFitxers.
     *
     */
    public DocumentPersistencia(String path){
        this.path = path;
        ioFitxers = new IOFitxers();
    }


    /** Es serialitza el document i es guarda al sistema.
     *
     * @param document Document a guardar (access object)
     * @see IOFitxers#escriureObjecte(Object, String) 
     */
    public void guardarDocument(DocumentAO document) throws ExceptionDirNoTrobat, ExceptionIO {
        String titolP = document.getTitol().replaceAll(" ", "-");
        String autorP = document.getAutor().replaceAll(" ", "-");
        try {
            ioFitxers.escriureObjecte(document, path + "/" + titolP + "_" + autorP + ".dat");
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut guardar el document: " + document);
        }
    }

    /** Retorna el document guardat al directori de persistencia documents amb el nom i autor especificats.
     *
     * @param autor Autor del document
     * @param titol Nom del document
     * @return Document obtingut (access object)
     * @see IOFitxers#llegirObjecte(String)  
     */
    public DocumentAO getDocument(String titol, String autor) throws ExceptionDirNoTrobat, ExceptionIO {
        String autorP = autor.replaceAll(" ", "-");
        String titolP = titol.replaceAll(" ", "-");
        String path1 = path + "/" + titolP + "_" + autorP + ".dat";
        try {
            return (DocumentAO) ioFitxers.llegirObjecte(path1);
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionIO("No s'ha pogut llegir el document: " + titolP + "_" + autorP + ".dat");
        }
    }

    /** Retorna una llista de tots els documents guardats al directori de persistencia documents.
     *
     * @return Llistat de documents access objects.
     * @see IOFitxers#llegirObjecte(String)
     */
    public ArrayList<DocumentAO> getDocuments() throws ExceptionDirNoTrobat, ExceptionIO {
        ArrayList<DocumentAO> docs = new ArrayList<>();
        File file = new File(path);
        for (File f: Objects.requireNonNull(file.listFiles())){
            try {
                docs.add((DocumentAO) ioFitxers.llegirObjecte(f.getPath()));
            } catch (IOException | ClassNotFoundException e) {
                throw new ExceptionIO("No s'ha pogut llegir el document: " + f.getName());
            }
        }

        return docs;
    }

    /** Elimina el document del directori de documents de persistencia especificat.
     *
     * @param autor Autor del document
     * @param titol Títol del document
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar el document
     */
    public void eliminarDocument(String titol, String autor) throws ExceptionDirectoriNoEliminat {
        autor = autor.replace(" ", "-");
        titol = titol.replace(" ", "-");
        String path1 = path + "/" + titol + "_" + autor + ".dat";
        File doc = new File(path1);
        if (doc.exists() && doc.isFile())
            if (!doc.delete()) throw new ExceptionDirectoriNoEliminat(path);
    }
}
