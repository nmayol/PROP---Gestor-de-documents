package domini;

import utils.Trie;
import Exceptions.*;

import java.util.ArrayList;

/**
 * @author Paula Grau
 * És un índex que guarda els autors.
 * La seva funció és facilitar la cerca d'autors a partir d'un prefix.
 */

public class IndexAutor {

    /**
     * Estructura en forma d'arbre que guarda els autors
     */
    private final Trie indexautor;

    /**
     * Creadora per defecte
     */
    public IndexAutor() {
        super();
        indexautor = new Trie();
    }

    public IndexAutor(Trie indexautor) {
        this.indexautor = indexautor;
    }

    public Trie getIndexautor() {
        return indexautor;
    }

    /**
     * Afegeix un autor a l'índex (no comprova que l'autor no existeixi ja a l'índex)
     * @param nom Nom de l'autor que volem afegir
     */
    public void afegirAutor_sempre(String nom) {
        indexautor.insert(nom);
    }

    /**
     * Afegeix un autor a l'índex
     * @param nom Nom de l'autor que volem afegir
     * @throws ExceptionAutorJaExisteix Salta quan l'autor ja existeix a l'índex
     */
    public void afegirAutor(String nom) throws ExceptionAutorJaExisteix {
        if (indexautor.search(nom)) throw new ExceptionAutorJaExisteix(nom);
        indexautor.insert(nom);
    }

    /**
     * Elimina un autor de l'índex
     * @param nom Nom de l'autor que volem eliminar
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix a l'índex
     */
    public void eliminarAutor(String nom) throws ExceptionAutorNoExisteix {
        if (!indexautor.search(nom)) throw new ExceptionAutorNoExisteix(nom);
        indexautor.remove(nom);
    }

    /**
     * Retorna els autors a partir d'un prefix
     * @param prefix Prefix per cercar autors (pot ser buit)
     * @return Retorna autors amb el prefix indicat
     * @throws ExceptionPrefixNoExisteix salta quan el prefix no existeix
     */
    public ArrayList<String> cercarAutors(String prefix) throws ExceptionPrefixNoExisteix {
        if (!indexautor.search_prefix(prefix)) throw new ExceptionPrefixNoExisteix(prefix);
        return indexautor.retornautors(prefix);
    }

    /**
     * Modifica el nom d'un autor
     * @param antic Nom antic de l'autor que volem modificar
     * @param nou Nom nou de l'autor
     * @throws ExceptionAutorNoExisteix Salta quan el nom antic de l'autor que volem modificar no existeix a l'índex
     * @throws ExceptionAutorJaExisteix Salta quan el nou nom de l'autor que volem assignar ja existeix a l'índex
     */
    public void canvi_Autor(String antic, String nou) throws ExceptionAutorNoExisteix, ExceptionAutorJaExisteix {
        if (!indexautor.search(antic)) throw new ExceptionAutorNoExisteix(antic);
        else if (indexautor.search(nou)) throw new ExceptionAutorJaExisteix(nou);
        else {
            indexautor.remove(antic);
            indexautor.insert(nou);
        }
    }


}