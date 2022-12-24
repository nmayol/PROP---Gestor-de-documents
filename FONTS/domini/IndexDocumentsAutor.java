package domini;

import Exceptions.*;

import java.util.*;

/**
 * @author Paula Grau
 * És un índex que guarda els autors i els títols de cadascun d'ells.
 * La seva funció és facilitar i agilitzar la cerca dels títols d'un autor.
 */

public class IndexDocumentsAutor {

    /**
     * Guarda els noms de tots els autors i un set<string> dels títols de cada autor
     */
    private final HashMap<String, Set<String>> autorTitols;

    /**
     * Creadora
     */
    public IndexDocumentsAutor() {
        super();
        autorTitols = new HashMap<>();
    }

    public IndexDocumentsAutor(HashMap<String, Set<String>> autorTitols) {
        this.autorTitols = autorTitols;
    }

    public HashMap<String, Set<String>> getAutorTitols() {
        return autorTitols;
    }

    /**
     * Afegeix un títol i autor d'un document a l'índex
     * @param autor Nom d'un autor
     * @param titol Nom d'un títol d'un document
     */
    public void afegeixTitolAutor(String autor, String titol) {
        if (!autorTitols.containsKey(autor)) { // l'autor encara no existia
            Set<String> titols = new HashSet<>();
            titols.add(titol);
            autorTitols.put(autor, titols);
        }
        else { // l'autor ja existeix
            autorTitols.get(autor).add(titol);
        }
    }

    /**
     * Elimina un títol i autor d'un document de l'índex
     * @param autor Nom d'un autor
     * @param titol Nom d'un títol d'un document
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix a l'índex
     * @throws ExceptionDocumentNoExisteix Salta quan el títol no existeix a l'índex
     */
    public void eliminarTitolAutor(String autor, String titol) throws ExceptionAutorNoExisteix, ExceptionDocumentNoExisteix {
        if (!autorTitols.containsKey(autor)) throw new ExceptionAutorNoExisteix(autor);
        if (!autorTitols.get(autor).contains(titol)) throw new ExceptionDocumentNoExisteix(autor, titol);
        autorTitols.get(autor).remove(titol);
        if (autorTitols.get(autor).isEmpty()) autorTitols.remove(autor);
    }

    /**
     * Retorna títols d'un autor
     * @param autor Nom d'un autor
     * @return Retorna títols de l'autor
     * @throws ExceptionAutorNoExisteix Salta quan l'autor no existeix a l'índex
     */
    public Set<String> buscarTitolsAutor(String autor) throws ExceptionAutorNoExisteix {
        if (!autorTitols.containsKey(autor)) throw new ExceptionAutorNoExisteix(autor);
        return autorTitols.get(autor);
    }
}