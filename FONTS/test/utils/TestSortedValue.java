/*
package test.utils;


import utils.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSortedValue {

    @Test
    public void test1() {
        Map<PairP<String, String>, Double> resultat_rebut = new HashMap<>();
        PairP<String, String> p = new PairP("a", "b");
        resultat_rebut.put(p, 1.0);
        PairP<String, String> p3 = new PairP<>("e", "f");
        resultat_rebut.put(p3, 3.0);
        PairP<String, String> p2 = new PairP<>("c", "d");
        resultat_rebut.put(p2, 2.0);

        Map<PairP<String, String>, Double> resultat_esperat = new HashMap<>();
        resultat_esperat.put(p3, 3.0);
        resultat_esperat.put(p2, 2.0);
        resultat_esperat.put(p, 1.0);

        SortedValue.sortByValue(resultat_rebut);

        assertEquals(resultat_esperat, resultat_rebut);
    }
}

 */
