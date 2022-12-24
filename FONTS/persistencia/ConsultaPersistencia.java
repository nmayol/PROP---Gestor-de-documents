package persistencia;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionIO;
import persistencia.accesObjects.ConsultaAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/** Aquesta classe s'encarrega de fer els accessos a disc de les consultes. Només treballa amb access objects.
 * @see ConsultaAO
 */
public class ConsultaPersistencia {

    private final IOFitxers ioFitxers;
    private final String path;

    /** Constructora. Inicialitza el path en el que treballa la classe i crea una instancia de IOFitxers.
     * @param path path consulta
     */
    public ConsultaPersistencia(String path){
        ioFitxers = new IOFitxers();
        this.path = path;
    }

    /** Guarda una consulta a la carpeta de consultes de l'usuari especificat.
     *
     * @param usuari Nom de l'usuari.
     * @param consultaAO Consulta access object
     * @param k Ordre, serveix per escriure el fitxer
     * @see IOFitxers#escriureObjecte(Object, String)
     * @throws ExceptionIO salta quan hi ha un error a persistencia
     * @throws ExceptionDirNoTrobat salta quan hi ha un error a persistencia
     */
    public void guardarConsulta(String usuari, ConsultaAO consultaAO, int k) throws ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + usuari + "/consultes";
        try {
            ioFitxers.escriureObjecte(consultaAO, path1 + "/" + consultaAO.getTipusConsulta().toString()+ "_" + k + ".dat");
        } catch (IOException e) {
            throw new ExceptionIO("No s'ha pogut guardar la consulta(" + consultaAO.toString());
        }
    }

    /** Retorna el conjunt de consultes guardades de un usuari.
     *
     * @param usuari Nom de l'usuari
     * @return Llista de consultes (access objects)
     * @see IOFitxers#llegirObjecte(String)
     */
    public ArrayList<ConsultaAO> getConsultes(String usuari) throws ExceptionDirNoTrobat, ExceptionIO {
        String path1 = path + "/" + usuari + "/consultes";
        ArrayList<ConsultaAO> consultes = new ArrayList<>();
        // try-with-resources
        try (Stream<Path> files = Files.list(Paths.get(path1)).sorted()){
            Path[] paths = files.toArray(Path[]::new);
            for (Path p: paths){
                try {
                    consultes.add((ConsultaAO) ioFitxers.llegirObjecte(p.toString()));
                } catch (IOException | ClassNotFoundException e) {
                    throw new ExceptionIO("No s'ha pogut llegir la consulta: " + p.toString());
                }
            }
        } catch (NotDirectoryException nde){
            throw new ExceptionDirNoTrobat(path);
        } catch (IOException ignored) {}

        return consultes;
    }
}
