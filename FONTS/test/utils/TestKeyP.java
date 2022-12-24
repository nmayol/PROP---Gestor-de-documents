package test.utils;

import utils.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class TestKeyP {

    @Test
    public void creadoraKeyP_notnull() {
        String s1 = new String();
        String s2 = new String();
        KeyP clau = new KeyP(s1, s2);
        assertNotNull(clau);
    }

    @Test
    public void KeyP_equals() {
        String s1 = new String();
        String s2 = new String();
        KeyP clau = new KeyP(s1, s2);
        boolean resultat_rebut = clau.equals(clau);

        assertEquals(true, resultat_rebut);
    }

    @Test
    public void KeyP_notequals() {
        String s1 = new String();
        String s2 = new String();
        KeyP clau = new KeyP(s1, s2);
        String s3 = new String();
        String s4 = new String();
        KeyP clau2 = new KeyP(s1, s2);

        boolean resultat_rebut = clau.equals(clau2);

        assertNotEquals(false, resultat_rebut);
    }
}
