package test.domini;
import Exceptions.ExceptionExpressioBooleanaIncorrecta;
import Exceptions.ExceptionNotPrimaryKeys;
import domini.Document;
import utils.ExpressioBooleana;
import Exceptions.ExceptionFormatNoValid;
import domini.IndexExpressionsBooleanes;
import org.junit.Test;
import utils.Format;
import utils.KeyP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestIndexExpressionsBooleanes {
    @Test
    public void afegirExpressioBuida() throws ExceptionExpressioBooleanaIncorrecta {
        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        int rebut = i.getExpressions().size();
        // CAS 1 No afegim res --> es buit i per tant te mida 0;
        assertEquals(0,rebut);
    }

    @Test
    public void afegirExpressio() throws ExceptionExpressioBooleanaIncorrecta {
        // CAS 2 Afegim una expressio --> la mida incrementa en 1
        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        String expr = "";
        i.afegirExpressio(expr);
        int rebut = i.getExpressions().size();
        assertEquals(1,rebut);
    }

    @Test
    public void afegirExpressioRepetida() throws ExceptionExpressioBooleanaIncorrecta {
        // CAS 3: Afegim una expressio que ja esta repetida --> la mida es mante
        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        i.afegirExpressio("p1|p2");
        i.afegirExpressio("p1|p2");
        int rebut = i.getExpressions().size();
        assertEquals(1,rebut);
    }


    @Test
    public void evaluarTotesExpressionsU () throws ExceptionNotPrimaryKeys, ExceptionExpressioBooleanaIncorrecta, ExceptionFormatNoValid {
        ArrayList<String> cont = new ArrayList<>(); cont.add("p1");
        HashSet<String> stopw = new HashSet<>();
        Document d = new Document(Format.txt,"hola","neus",cont);

        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        i.afegirExpressio("(a&b&c)");
        i.afegirExpressio("p1 | p2 | p3");

        i.evaluarTotesExpressions(d);

        // CAS 1: La expressio s'ha afegit perque es certa
        ExpressioBooleana e2 = new ExpressioBooleana();
        e2.ConstrueixArbre("p1|p2|p3",e2.getExpressio().getRoot());
        ArrayList<String[]> rebut2 = i.getDocumentsExpressio(e2);
        assertEquals(1,rebut2.size());


    }

    @Test
    public void evaluarTotesExpressionsZero () throws ExceptionNotPrimaryKeys, ExceptionExpressioBooleanaIncorrecta, ExceptionFormatNoValid {
        ArrayList<String> cont = new ArrayList<>(); cont.add("p1");
        HashSet<String> stopw = new HashSet<>();
        Document d = new Document(Format.txt,"hola","neus",cont);

        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        i.afegirExpressio("(a&b&c)");
        i.afegirExpressio("p1 | p2 | p3");

        i.evaluarTotesExpressions(d);

        // CAS 2: La expressio no s'ha afegit perque es falsa
        ExpressioBooleana e1 = new ExpressioBooleana();
        e1.ConstrueixArbre("(a&b&c)",e1.getExpressio().getRoot());
        ArrayList<String[]> rebut1 = i.getDocumentsExpressio(e1);
        assertEquals(0,rebut1.size());

    }

    @Test
    public void esborratDocument() throws ExceptionNotPrimaryKeys,ExceptionExpressioBooleanaIncorrecta, ExceptionFormatNoValid{
        ArrayList<String> cont = new ArrayList<>(); cont.add("p1");
        Document d = new Document(Format.txt,"hola","neus",cont);
        Document d1 = new Document(Format.txt,"hola","paula",cont);

        ExpressioBooleana e = new ExpressioBooleana("p1 | p2 | p3");
        IndexExpressionsBooleanes i = new IndexExpressionsBooleanes();
        i.afegirExpressio("p1 | p2 | p3");

        i.evaluarTotesExpressions(d);
        i.evaluarTotesExpressions(d1);
        i.esborratDocument(d);
        int rebut = i.getDocumentsExpressio(e).size();

        assertEquals(1,rebut);

    }




}