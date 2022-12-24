package persistencia.accesObjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/** Representa una instancia de la classe IndexDocumentsAutor per guardar a disc. Implementa la interficie Serializable per poder guardar-la a disc.
 * Conte la classe Trie, que tambe implementa Serializable.
 * @see domini.IndexDocumentsAutor
 */
public class IndexDocumentsAutorAO implements Serializable {
    private HashMap<String, Set<String>> autorTitols;

    public IndexDocumentsAutorAO(HashMap<String, Set<String>> autorTitols) {
        this.autorTitols = autorTitols;
    }

    public HashMap<String, Set<String>> getAutorTitols() {
        return autorTitols;
    }

    public void setAutorTitols(HashMap<String, Set<String>> autorTitols) {
        this.autorTitols = autorTitols;
    }
}
