package test.utils;

import utils.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class TestTrie {

    @Test
    public void retornautors() {

        // CAS 1: llistar tots els autors afegits
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("joan");
        resultat_esperat.add("joana");
        resultat_esperat.add("joaquim");
        Trie t = new Trie();
        t.insert("joan");
        t.insert("joaquim");
        t.insert("joana");

        ArrayList<String> resultat_rebut = t.retornautors("joa");
        assertEquals(resultat_esperat, resultat_rebut);

        // CAS 2: no llista cap autor perque no existeix el prefix al trie
        resultat_esperat.clear();
        resultat_rebut.clear();

        Trie t2 = new Trie();
        t2.insert("marc");

        resultat_rebut = t2.retornautors("joa");
        assertEquals(resultat_esperat, resultat_rebut);

        // CAS 3: llista tots els autors, pero no eliminats no
        resultat_esperat.clear();
        resultat_rebut.clear();

        resultat_esperat.add("joan");
        resultat_esperat.add("paula");
        Trie t3 = new Trie();
        t3.insert("neus");
        t3.insert("joan");
        t3.insert("paula");
        t3.remove("neus");

        resultat_rebut = t3.retornautors("");
        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void removeautor() {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("dins");
        //resultat_esperat.add("dintre");

        Trie t = new Trie();
        t.insert("marxa");
        t.insert("dins");
        t.insert("dintre");
        t.remove("marxa");
        t.remove("dintre");
        ArrayList<String> resultat_rebut = t.retornautors("");

        assertEquals(resultat_esperat, resultat_rebut);

        // afegir pal i palau i eliminar pal
        resultat_esperat.clear();
        resultat_rebut.clear();

        resultat_esperat.add("palau");

        Trie t2 = new Trie();
        t2.insert("pal");
        t2.insert("palau");
        t2.remove("pal");
        resultat_rebut = t2.retornautors("");

        assertEquals(resultat_esperat, resultat_rebut);

        // afegir pal i palau i eliminar palau
        resultat_esperat.clear();
        resultat_rebut.clear();

        resultat_esperat.add("pal");

        Trie t3 = new Trie();
        t3.insert("pal");
        t3.insert("palau");
        t3.remove("palau");
        resultat_rebut = t3.retornautors("");

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void search_paraula() {
        Trie t = new Trie();
        t.insert("neus");
        boolean resultat_rebut = t.search("neus");

        assertEquals(true, resultat_rebut);
    }

    @Test
    public void search_prefix() {
        Trie t = new Trie();
        t.insert("neus");
        boolean resultat_rebut = t.search_prefix("n");

        assertEquals(true, resultat_rebut);
    }

    @Test
    public void cercaeliminada() {
        Trie t = new Trie();
        t.insert("neus");
        boolean resultat_rebut = t.search("neus");

        assertEquals(true, resultat_rebut);

        t.remove("neus");
        boolean resultat_rebut2 = t.search("neus");

        assertEquals(false, resultat_rebut2);
    }

}
