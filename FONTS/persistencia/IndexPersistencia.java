package persistencia;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionIO;
import persistencia.accesObjects.IndexAutorAO;
import persistencia.accesObjects.IndexDocumentsAutorAO;
import persistencia.accesObjects.IndexExpressionsBooleanesAO;

import java.io.File;
import java.io.IOException;

/** Aquesta classe s'encarrega de fer tots els accessos a disc dels tres indexs de l'aplicacio. Nomes treballa amb access objects.
 * @see IndexAutorAO
 * @see IndexDocumentsAutorAO
 * @see IndexExpressionsBooleanesAO
 */
public class IndexPersistencia {

    private final IOFitxers ioFitxers;
    private final String path;

    /** Constructora. Inicialitza el path en el que treballa la classe i crea una instancia de IOFitxers.
     *
     */
    public IndexPersistencia(String path){
        this.path = path;
        ioFitxers = new IOFitxers();
    }

    /** Guarda l'index a la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @param ia Index access object
     * @see IOFitxers#escriureObjecte(Object, String) 
     */
    public void guardarIndexAutor(String usuari, IndexAutorAO ia) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexAutor.dat";
        try {
            ioFitxers.escriureObjecte(ia, path1);
        } catch (IOException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut guardar l'index d'autors");
        }
    }

    /** Retorna l'index corresponent de la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @return IndexAutorAO
     * @see IOFitxers#llegirObjecte(String)
     */
    public IndexAutorAO getIndexAutor(String usuari) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexAutor.dat";
        try {
            return (IndexAutorAO) ioFitxers.llegirObjecte(path1);
        } catch (IOException | ClassNotFoundException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut llegir l'index d'autors");
        }
    }

    /** Guarda l'index a la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @param ida Index
     * @see IOFitxers#escriureObjecte(Object, String)
     */
    public void guardarIndexDocumentsAutor(String usuari, IndexDocumentsAutorAO ida) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexDocumentsAutor.dat";
        try {
            ioFitxers.escriureObjecte(ida, path1);
        } catch (IOException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut llegir l'index de documents");
        }
    }

    /** Retorna l'index corresponent de la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @return IndexDocumentsAutor
     * @see IOFitxers#llegirObjecte(String)
     */
    public IndexDocumentsAutorAO getIndexDocumentsAutor(String usuari) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexDocumentsAutor.dat";
        try {
            return (IndexDocumentsAutorAO) ioFitxers.llegirObjecte(path1);
        } catch (IOException | ClassNotFoundException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut llegir l'index de documents");
        }
    }

    /** Guarda l'index a la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @param ieb Index
     * @see IOFitxers#escriureObjecte(Object, String)
     */
    public void guardarIndexExpressionsBooleanes(String usuari, IndexExpressionsBooleanesAO ieb) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexExpressionsBooleanes.dat";
        try {
            ioFitxers.escriureObjecte(ieb, path1);
        } catch (IOException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut guardar l'index d'expressions booleanes");
        }
    }

    /** Retorna l'index corresponent de la carpeta de l'usuari corresponent.
     *
     * @param usuari Nom de l'usuari
     * @return IndexExpressionsBooleanes
     * @see IOFitxers#llegirObjecte(String)
     */
    public IndexExpressionsBooleanesAO getIndexExpressionsBooleanes(String usuari) throws ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        String path1 = path + "/" + usuari + "/index" + "/indexExpressionsBooleanes.dat";
        try {
            return (IndexExpressionsBooleanesAO) ioFitxers.llegirObjecte(path1);
        } catch (IOException | ClassNotFoundException e) {
            ioFitxers.clearDirectori(new File(path + "/" + usuari + "/index"));
            throw new ExceptionIO("No s'ha pogut llegir l'index d'expressions booleanes");
        }
    }
}
