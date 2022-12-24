package test.utils;

import utils.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class TestPairP {

    @Test
    public void creadoraPairP_notnull() {
        Object o1 = new Object();
        Object o2 = new Object();
        PairP parella = new PairP(o1, o2);
        assertNotNull(parella);
    }

    @Test
    public void creadoraPairP_parametrescorrectes() {
        ArrayList<Object> resultat_esperat = new ArrayList<>();
        Object o1 = new Object();
        Object o2 = new Object();
        resultat_esperat.add(o1);
        resultat_esperat.add(o2);

        ArrayList<Object> resultat_rebut = new ArrayList<>();
        PairP pair_resultant = new PairP(o1, o2);
        resultat_rebut.add(pair_resultant.getFirst());
        resultat_rebut.add(pair_resultant.getSecond());

        assertEquals(resultat_esperat, resultat_rebut);
    }
}
