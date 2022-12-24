package utils;

import java.io.Serializable;
import java.util.*;

/**
 * @author Paula Grau
 * Estructura de dades per implementar un trie per guardar noms i facilitar la cerca segons el prefix.
 */
public class Trie implements Serializable {

    /**
     * Estructura d'un node del trie.
     */
    // estructura de cada node del Trie
    private static class TrieNode implements Serializable{
        /**
         * Guarda lletres de noms i un punter al node on es troba la següent lletra
         */
        HashMap<Character, TrieNode> children;
        /**
         * Indica si la lletra que apunta a aquell node és un final de paraula
         */
        boolean endOfWord;
        /**
         * Creadora d'un TrieNode
         */
        public TrieNode() {
            children = new HashMap<>();
            endOfWord = false;
        }
    }

    /**
     * Node arrel del Trie
     */
    private final TrieNode root;

    /**
     * Creadora per defecte
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Insereix una paraula al trie
     * @param word Paraula que volem afegir al trie
     */
    public void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) {
                node = new TrieNode();
                current.children.put(c, node);
            }
            current = node;
        }
        current.endOfWord = true;
    }

    /**
     * Elimina una paraula de longitud index a partir del node current
     * @param current Node a partir del qual s'elimina la paraula
     * @param word Paraula que volem eliminar
     * @param index Mida de la paraula que volem eliminar
     * @return Indica si la paraula s'ha pogut eliminar correctament
     */
    // Eliminar autor del Trie
    public boolean removeautor(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.endOfWord) return false;
            current.endOfWord = false;
            return current.children.isEmpty();
        }
        char c = word.charAt(index);
        TrieNode node = current.children.get(c);
        if (node == null) return false;
        boolean shouldDelte = removeautor(node, word, index+1) && !current.endOfWord;

        if (shouldDelte) {
            current.children.remove(c);
            return current.children.isEmpty();
        }
        return false;
    }

    /**
     * Elimina una paraula del trie
     * @param word Paraula que volem eliminar
     * @return Indica si la paraula s'ha pogut eliminar correctament
     */
    public boolean remove(String word) {
        return removeautor(root, word, 0);
    }

    /**
     * Busca una paraula al Trie
     * @param word Paraula que volem buscar
     * @return Indica si la paraula es troba al trie
     */
    // Buscar paraules al Trie
    public boolean search(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) return false;
            current = node;
        }
        return current.endOfWord;
    }

    /**
     * Busca un prefix al trie
     * @param prefix Prefix que volem buscar
     * @return Indica si el prefix es troba al trie
     */
    public boolean search_prefix(String prefix) {
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); ++i) {
            char c = prefix.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) return false;
            current = node;
        }
        return true;
    }

    /**
     * Retorna autors amb prefix actual a partir del node current desordenats alfabèticament
     * @param actual Prefix a partir del qual busquem noms del trie
     * @param current Node a partir del qual es busquen autors
     * @return Retorna autors amb prefix actual a partir del node current
     */
    public ArrayList<String> rec_desordenat(String actual, TrieNode current) {
        ArrayList<String> autorsperprefix = new ArrayList<>();
        if (current.endOfWord) autorsperprefix.add(actual);
        for (Map.Entry<Character, TrieNode> e : current.children.entrySet()) {
            autorsperprefix.addAll(rec_desordenat(actual + e.getKey(), e.getValue()));
        }
        return autorsperprefix;
    }

    /**
     * Retorna autors amb prefix actual a partir del node current ordenats alfabèticament
     * @param actual Prefix a partir del qual busquem noms del trie
     * @param current Node a partir del qual es busquen autors
     * @return Retorna autors amb prefix actual a partir del node current
     */
    public ArrayList<String> rec(String actual, TrieNode current) {
        ArrayList<String> autorsperprefix = new ArrayList<String>();
        if (current.endOfWord) autorsperprefix.add(actual);
        int ch = 'a';
        for (int i = 0; i < 26; ++i) {
            int index = (int)ch + i;
            char c = (char)index;
            TrieNode next = current.children.get(c);
            if (next != null) {
                ArrayList<String> paraules_noves = rec(actual + c, next);
                autorsperprefix.addAll(paraules_noves);
            }
        }
        return autorsperprefix;
    }

    /**
     * Retorna autors que contenen el prefix
     * @param prefix Prefix a partir del qual busquem autors
     * @return Retorna dels autors que contenen el prefix
     */
    public ArrayList<String> retornautors(String prefix) {
        ArrayList<String> autorsperprefix = new ArrayList<>();
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); ++i) {
            char c = prefix.charAt(i);
            if (current.children.containsKey(c)) current = current.children.get(c);
            else return autorsperprefix;
        }
        //autorsperprefix.addAll(rec(actual, current));
        autorsperprefix.addAll(rec_desordenat(prefix, current));
        return autorsperprefix;
    }
}