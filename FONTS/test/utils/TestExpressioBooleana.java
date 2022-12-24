package test.utils;
import Exceptions.ExceptionExpressioBooleanaIncorrecta;
import org.junit.Test;
import utils.ExpressioBooleana;
import utils.*;
import static org.junit.Assert.*;
import java.util.ArrayList;


public class TestExpressioBooleana {

    @Test
    public void tradueixExpressio() throws ExceptionExpressioBooleanaIncorrecta {
        String resultat_esperat = "(p1&p2&p3)&(\"hola adeu\"|pep)&!joan";
        String resultat_rebut = "";

        ExpressioBooleana e = new ExpressioBooleana("{p1 p2 p3} & (\"hola adeu\" | pep) & !joan");
        resultat_rebut = e.tradueixExpressio("{p1 p2 p3} & (\"hola adeu\" | pep) & !joan");
        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void ConstrueixArbre() throws ExceptionExpressioBooleanaIncorrecta {
        String resultat_esperat = "&&&&p1p2p3|hola adeupep!joan";
        String resultat_rebut = "";
        Tree<String> t = new Tree<String>();
        ExpressioBooleana e = new ExpressioBooleana("{p1 p2 p3} & (\"hola adeu\" | pep) & !joan");
        e.ConstrueixArbre("{p1 p2 p3} & (\"hola adeu\" | pep) & !joan",t.getRoot());
        resultat_rebut = e.getArbre(e.getExpressio().getRoot());
        assertEquals(resultat_esperat, resultat_rebut);

    }

    @Test
    public void BuscarInici() throws ExceptionExpressioBooleanaIncorrecta {
        int resultat_esperat = 28;
        ExpressioBooleana e = new ExpressioBooleana("(p1&p2&p3)&(\"hola adeu\"|pep)&!joan");
        int resultat_rebut = e.BuscarInici("(p1&p2&p3)&(\"hola adeu\"|pep)&!joan");
        assertEquals(resultat_esperat,resultat_rebut);

    }


    @Test
    public void BuscarIniciNonHiHa() throws ExceptionExpressioBooleanaIncorrecta {
        int resultat_esperat = -1;
        ExpressioBooleana e = new ExpressioBooleana("p1");
        int resultat_rebut = e.BuscarInici("p1");
        assertEquals(resultat_esperat,resultat_rebut);

    }


    @Test
    public void evaluateRec1() throws ExceptionExpressioBooleanaIncorrecta {
        boolean resultat_rebut;
        boolean resultat_esperat;

        ArrayList<String> cont = new ArrayList<>();
        cont.add("hola");
        cont.add("adeu");
        cont.add("aaaa");
        cont.add("p2");
        cont.add("joan");

        // CAS 1: Detecta una expressio com a falsa
        ExpressioBooleana e = new ExpressioBooleana("(p1&p2&p3)&(\"hola adeu\"|pep)&!joan");
        resultat_rebut = e.evaluateRec(cont,e.getExpressio().getRoot());
        resultat_esperat = false;
        assertEquals(resultat_esperat,resultat_rebut);
    }


    @Test
    public void evaluateRec2() throws ExceptionExpressioBooleanaIncorrecta {
        boolean resultat_rebut;
        boolean resultat_esperat;

        ArrayList<String> cont = new ArrayList<>();
        cont.add("hola");
        cont.add("adeu");
        cont.add("aaaa");
        cont.add("p2");
        cont.add("joan");

        // CAS 2: Expressions entre cometes avaluades correctament (com a paraules consequents)
        ExpressioBooleana e2 = new ExpressioBooleana("\"hola adeu\"");
        resultat_rebut = e2.evaluateRec(cont,e2.getExpressio().getRoot());
        resultat_esperat = true;
        assertEquals(resultat_esperat,resultat_rebut);

    }


    @Test
    public void evaluateRec3() throws ExceptionExpressioBooleanaIncorrecta {
        boolean resultat_rebut;
        boolean resultat_esperat;

        ArrayList<String> cont = new ArrayList<>();
        cont.add("hola");
        cont.add("adeu");
        cont.add("aaaa");
        cont.add("p2");
        cont.add("joan");

        // CAS 4: Avalua les expressions en subconjunts certes
        ExpressioBooleana e4 = new ExpressioBooleana("{p1 p2 p3}");
        resultat_rebut = e4.evaluateRec(cont,e4.getExpressio().getRoot());
        resultat_esperat = false;
        assertEquals(resultat_esperat,resultat_rebut);



    }



    @Test
    public void evaluateRec4() throws ExceptionExpressioBooleanaIncorrecta {
        boolean resultat_rebut;
        boolean resultat_esperat;

        ArrayList<String> cont = new ArrayList<>();
        cont.add("hola");
        cont.add("adeu");
        cont.add("aaaa");
        cont.add("p2");
        cont.add("joan");

        // CAS 5: Avalua les expressions en subconjunts
        cont.add("p1"); cont.add("p3");
        ExpressioBooleana e5 = new ExpressioBooleana("{p1 p2 p3}");
        resultat_rebut = e5.evaluateRec(cont,e5.getExpressio().getRoot());
        resultat_esperat = true;
        assertEquals(resultat_esperat,resultat_rebut);

    }




    @Test
    public void evaluateRec5() throws ExceptionExpressioBooleanaIncorrecta {
        boolean resultat_rebut;
        boolean resultat_esperat;

        ArrayList<String> cont = new ArrayList<>();
        cont.add("hola");
        cont.add("adeu");
        cont.add("aaaa");
        cont.add("p2");
        cont.add("joan");
        cont.add("p1");
        cont.add("p3");


        ExpressioBooleana e6 = new ExpressioBooleana("!(p4 | p5 | p6)");
        resultat_rebut = e6.evaluateRec(cont,e6.getExpressio().getRoot());
        resultat_esperat = true;
        assertEquals(resultat_esperat,resultat_rebut);

    }


    @Test
    public void getArbre() throws ExceptionExpressioBooleanaIncorrecta {
        ExpressioBooleana e = new ExpressioBooleana("{p1 p2 p3}& (\"hola adeu\"|pep) &!joan");
        String resultat_rebut = e.getArbre(e.getExpressio().getRoot());
        String resultat_esperat = "&&&&p1p2p3|hola adeupep!joan";
        assertEquals(resultat_esperat,resultat_rebut);
    }



    @Test(expected = ExceptionExpressioBooleanaIncorrecta.class)
    public void ExpressioBooleana() throws ExceptionExpressioBooleanaIncorrecta {
        ExpressioBooleana expr = new ExpressioBooleana("())");
    }

}
