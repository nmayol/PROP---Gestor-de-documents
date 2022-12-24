package persistencia.accesObjects;

import utils.Trie;

import java.io.Serializable;

/** Representa una instancia de la classe IndexAutor per guardar a disc. Implementa la interficie Serializable per poder guardar-la a disc.
 * Conte la classe Trie, que tambe implementa Serializable.
 * @see domini.IndexAutor
 */
public class IndexAutorAO implements Serializable {
    private Trie indexAutor;

    public IndexAutorAO(Trie indexAutor) {
        this.indexAutor = indexAutor;
    }

    public Trie getIndexAutor() {
        return indexAutor;
    }

    public void setIndexAutor(Trie indexAutor) {
        this.indexAutor = indexAutor;
    }
}
