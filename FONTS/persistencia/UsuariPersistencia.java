package persistencia;

import Exceptions.*;
import persistencia.accesObjects.DocumentAO;
import utils.PairP;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/** Aquesta classe s'encarrega de fer els accessos a disc dels documents. No treballa amb objectes, ja que no es guarden les instancies dels usuaris, sino la informacio de cadascun d'ells.
 * Per cada usuari es crea un directori a la carpeta de persistencia on es guarda la seva informacio, historials i índexs.
 */
public class UsuariPersistencia {

    private final IOFitxers ioFitxers;
    private final String path;

    /** Constructora. Inicialitza el path en el que treballa la classe i crea una instancia de IOFitxers.
     *
     */
    public UsuariPersistencia(String path){
        this.path = path;
        ioFitxers = new IOFitxers();
    }

    /** Es guarda el nom i contrasenya de l'usuari.
     * Si l'usuari no estava ja guardat, es crea la seva estructura
     * de directoris de persistencia.
     *
     * @param nom Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @throws ExceptionDirectoriNoCreat No s'ha pogut crear un cert directori
     * @throws ExceptionUsuariJaExisteix Ja hi ha un usuari guardat amb el nom aportat
     * @see IOFitxers#crearDirectori(String) 
     * @see IOFitxers#escriureFitxer(String, String, boolean) 
     */
    public void nouUsuari(String nom, String contrasenya) throws ExceptionDirectoriNoCreat, ExceptionUsuariJaExisteix, ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + nom;
        File dir = new File(path1);
        if(!dir.exists()){
            ioFitxers.crearDirectori(path1);
            ioFitxers.crearDirectori(path1 + "/consultes");
            ioFitxers.crearDirectori(path1 + "/index");
            try {
                ioFitxers.escriureFitxer(path1 + "/llistaDocs.txt", "--", false);
            } catch (IOException e) {
                throw new ExceptionIO("No s'ha pogut escriure la informacio dels documents de l'usuari a disc");
            }
            try {
                ioFitxers.escriureFitxer(path1 + "/info.txt", contrasenya + "\n" + nom + "\n", false);
            } catch (IOException e) {
                throw new ExceptionIO("No s'ha pogut escriure la informacio del usuari a disc");
            }
        }else throw new ExceptionUsuariJaExisteix(nom);
    }

    /** Elimina tots els directoris de l'usuari especificat.
     *
     * @param nom Nom de l'usuari.
     * @throws ExceptionDirectoriNoEliminat No s'ha pogut eliminar un directori.
     * @see IOFitxers#eliminarDirectori(String) 
     */
    public void eliminarUsuari(String nom) throws ExceptionDirectoriNoEliminat, ExceptionUsuariNoExisteix {
        String path1 = path + "/" + nom;
        File dir = new File(path1);
        if(dir.exists()){
            ioFitxers.eliminarDirectori(path1);
        }else throw new ExceptionUsuariNoExisteix(nom);
    }

    /** Log d'inici de sessio al document info.txt per l'usuari especificat
     *
     * @param nom Nom de l'usuari
     * @see IOFitxers#escriureFitxer(String, String, boolean) 
     */
    public void iniciarSessio(String nom) throws ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + nom + "/info.txt";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            ioFitxers.escriureFitxer(path1, "\nInici de sessio " + dtf.format(now), true);
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut escriure al fitxer d'informacio de l'usuari");
        }
    }

    /** Log de tancament de sessio al document info.txt per l'usuari especificat
     *
     * @param nom Nom de l'usuari
     * @see IOFitxers#escriureFitxer(String, String, boolean) 
     */
    public void tancarSessio(String nom) throws ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + nom + "/info.txt";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            ioFitxers.escriureFitxer(path1, "\nTancament de sessio " + dtf.format(now), true);
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut escriure al fitxer d'informacio de l'usuari");
        }
    }

    /** Elimina un document de la llista de documents de l'usuari especificat.
     *
     * @param usuari Nom de l'usuari
     * @param titol Títol del document
     * @param autor Autor del document
     */
    public void eliminarDoc(String usuari, String titol, String autor) throws ExceptionDirNoTrobat, ExceptionIO {
        ArrayList<PairP<String, String>> llista = getLlistaDocsUsuari(usuari);
        String path1 = path + "/" + usuari +"/llistaDocs.txt";
        try {
            ioFitxers.escriureFitxer(path1, "--", false);
            llista.remove(new PairP<>(titol, autor));
            for (PairP<String, String> l : llista) {
                ioFitxers.escriureFitxer(path1, "\n" + l.getFirst() + "\n" + l.getSecond() + "\n-", true);
            }
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut escriure la informacio dels documents de l'usuari a disc");
        }
    }

    /** Afegeix un document a la llista de documents de l'usuari especificat.
     *
     * @param usuari Nom de l'usuari
     * @param titol Títol del document
     * @param autor Autor del document
     * @see IOFitxers#escriureFitxer(String, String, boolean)
     */
    public void guardarDoc(String usuari, String titol, String autor) throws ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + usuari + "/llistaDocs.txt";
        try {
            ioFitxers.escriureFitxer(path1, "\n" + titol + "\n" + autor + "\n-", true);
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut escriure la informacio dels documents de l'usuari a disc");
        }
    }

    /** Retorna la llista de documents de l'usuari especificat.
     *
     * @param nom Nom de l'usuari
     * @return Llista de pairs (títol, autor)
     */
    public ArrayList<PairP<String, String>> getLlistaDocsUsuari(String nom) throws ExceptionDirNoTrobat {
        ArrayList<PairP<String, String>> llista = new ArrayList<>();
        String path1 = path + "/" + nom;
        File file = new File(path1 + "/llistaDocs.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            if(sc.nextLine().equals("--")) {
                while (sc.hasNextLine()) {
                    String n = sc.nextLine();
                    String d = sc.nextLine();
                    String salt = sc.nextLine();
                    if (!salt.equals("-")) sc.close();

                    llista.add(new PairP<>(n, d));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ExceptionDirNoTrobat(path1);
        }

        return llista;
    }

    /** Comprova si algun usuari estava utilitzant el document, a traves de les llistes de documents de cada usuari.
     *
     * @param autor Autor del document
     * @param titol Títol del document
     * @return boolean True si esta en us, false al contari
     */
    public boolean docUtilitzat(String titol, String autor) throws ExceptionDirNoTrobat {
        ArrayList<PairP<String, String>> llista;
        File file = new File(path);
        for (File f: Objects.requireNonNull(file.listFiles())){
            llista = getLlistaDocsUsuari(f.getName());
            for (PairP<String, String> p: llista){
                if (p.getFirst().equals(titol) && p.getSecond().equals(autor)) return true;
            }
        }
        return false;
    }
}
