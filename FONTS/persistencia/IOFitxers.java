package persistencia;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoCreat;
import Exceptions.ExceptionDirectoriNoEliminat;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

/** Aquesta classe recull totes les operacions de manipulació de fitxers i directoris a disc. Només treballa amb access objects.
 *
 */
public class IOFitxers {

    /** Crea un directori amb la ruta especificada si no existeix.
     *
     * @param path Ruta
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear el directori.
     */
    public void crearDirectori(String path) throws ExceptionDirectoriNoCreat {
        File dir = new File(path);
        if(!dir.exists())
            if(!dir.mkdirs()) throw new ExceptionDirectoriNoCreat(path);
    }

    /** Elimina el directori amb la ruta especificada.
     *
     * @param path Ruta
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar el directori
     */
    public void eliminarDirectori(String path) throws ExceptionDirectoriNoEliminat{
        File file = new File(path);
        if (file.exists()){
            clearDirectori(file);
            if (!file.delete()) throw new ExceptionDirectoriNoEliminat(file.getPath());
        }
    }

    /** Elimina tots els directoris i fitxers dins del directori especificat.
     *
     * @param file Directori
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar un directori
     */
    public void clearDirectori(File file) throws ExceptionDirectoriNoEliminat {
        File[] files = file.listFiles();
        for (File f: Objects.requireNonNull(files)){
            if(f.isDirectory()) clearDirectori(f);
            if(!f.delete()) throw new ExceptionDirectoriNoEliminat(f.getPath());
        }
    }

    /** Escriu a disc una instància de la classe Object al directori amb la ruta especificada.
     *
     * @param o Objecte
     * @param path Ruta
     * @throws IOException salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     */
    public void escriureObjecte(Object o, String path) throws ExceptionDirNoTrobat, IOException {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            fos.close();
        } catch (FileNotFoundException fe){
            throw new ExceptionDirNoTrobat(path);
        }
    }

    /** Llegeix de disc una instància de la classe Object amb la ruta especificada.
     *
     * @param path Ruta
     * @return Instància de la classe Object, si no s'ha pogut trobar es retorna null.
     * @throws ClassNotFoundException salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     * @throws IOException salta quan hi ha un error a persistencia
     */
    public Object llegirObjecte(String path) throws ExceptionDirNoTrobat, IOException, ClassNotFoundException {
        Object o = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream os = new ObjectInputStream(fis);
            o = os.readObject();
            os.close();
            fis.close();
        } catch (FileNotFoundException fe){
            throw new ExceptionDirNoTrobat(path);
        }
        return o;
    }

    /** Llegir el fitxer de text especificat.
     *
     * @param path Ruta
     */
    public String llegirfitxer(String path) throws FileNotFoundException {
        File file = new File(path);
        String text = "";
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) text = text.concat(sc.nextLine() + "\n");
        return text;
    }

    /** Escriu al fitxer de text especificat.
     *
     * @param path Ruta
     * @param text Text a escriure
     * @param append True: Adjuntar, False: sobreescriure
     */
    public void escriureFitxer(String path, String text, boolean append) throws ExceptionDirNoTrobat, IOException {
        File file = new File(path);
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(file, append));
            pw.append(text);
            pw.close();
        } catch (FileNotFoundException fe) {
            throw new ExceptionDirNoTrobat(path);
        }
    }
}
