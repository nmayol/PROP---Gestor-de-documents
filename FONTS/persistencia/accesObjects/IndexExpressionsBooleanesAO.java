package persistencia.accesObjects;

import utils.ExpressioBooleana;
import utils.KeyP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/** Representa una instancia de la classe IndexExpressionsBooleanes per guardar a disc. Implementa la interficie Serializable per poder guardar-la a disc.
 * Conte un hash map amb classe ExpressioBooleana, que tambe implementa Serializable.
 * @see domini.IndexDocumentsAutor
 */
public class IndexExpressionsBooleanesAO implements Serializable {
    private HashMap<ExpressioBooleana, Set<KeyP>> Index;

    public IndexExpressionsBooleanesAO(HashMap<ExpressioBooleana, Set<KeyP>> index) {
        Index = index;
    }

    public HashMap<ExpressioBooleana, Set<KeyP>> getIndex() {
        return Index;
    }

    public void setIndex(HashMap<ExpressioBooleana, Set<KeyP>> index) {
        Index = index;
    }
}
