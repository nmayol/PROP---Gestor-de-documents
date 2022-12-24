package test.domini;

import Exceptions.ExceptionConsultaLimitParametres;
import domini.Consulta;
import static org.junit.Assert.*;
import utils.TipusConsulta;
import org.junit.Test;
import java.util.ArrayList;

public class TestConsulta {

    Consulta consultaTest = null;

    @Test
    public void testConsutrutora() throws ExceptionConsultaLimitParametres {
        ArrayList<String> par = new ArrayList<>();
        par.add("Hola");
        // par < MAX SIZE -> No excepcio
        consultaTest = new Consulta(par, TipusConsulta.docsAutor);
        assertNotNull(consultaTest);
    }

    @Test(expected = ExceptionConsultaLimitParametres.class)
    public void testConstructoraExecpcio() throws ExceptionConsultaLimitParametres{
        ArrayList<String> par = new ArrayList<>();
        par.add("Par1");
        par.add("Par2");
        consultaTest = new Consulta(par, TipusConsulta.docsAutor);
        assertNull(consultaTest);
    }

    @Test
    public void testNouParametre() throws ExceptionConsultaLimitParametres {
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> esperat = new ArrayList<>();
        par.add("a");
        esperat.add("a");

        // Consulta de dos parametres, hi cap un altre
        consultaTest = new Consulta(par, TipusConsulta.docsKSemblants);
        consultaTest.nouParametre("b");
        esperat.add("b");

        assertEquals(esperat, consultaTest.getParametres());
    }

    @Test(expected = ExceptionConsultaLimitParametres.class)
    public void testNouParametreExcepcio() throws ExceptionConsultaLimitParametres {
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> esperat = new ArrayList<>();
        par.add("a");
        esperat.add("a");
        consultaTest = new Consulta(par, TipusConsulta.docsAutor);

        // Ja no caben mes -> excepcio
        consultaTest.nouParametre("c");

        assertEquals(esperat, consultaTest.getParametres());
    }

    @Test
    public void testToString() throws ExceptionConsultaLimitParametres {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        ArrayList<String> resultat_rebut = new ArrayList<>();

        ArrayList<String> par = new ArrayList<>();
        par.add("a");
        consultaTest = new Consulta(par, TipusConsulta.autorsPrefix);
        resultat_esperat.add("Prefix: [a]");
        resultat_rebut.add(consultaTest.toString());

        consultaTest = new Consulta(par, TipusConsulta.docsAutor);
        resultat_esperat.add("Autor: [a]");
        resultat_rebut.add(consultaTest.toString());

        par.add("b");
        par.add("1");
        consultaTest = new Consulta(par, TipusConsulta.docsKSemblants);
        resultat_esperat.add("Autor, titol, numero: [a, b, 1]");
        resultat_rebut.add(consultaTest.toString());

        par.clear();
        par.add("a");
        par.add("Hola");
        consultaTest = new Consulta(par, TipusConsulta.docsPC);
        resultat_esperat.add("Paraules clau: [a, Hola]");
        resultat_rebut.add(consultaTest.toString());

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void testCheckNumParametres() throws ExceptionConsultaLimitParametres {
        ArrayList<Integer> resultat_esperat = new ArrayList<>();
        ArrayList<Integer> resultat_rebut = new ArrayList<>();

        ArrayList<String> par = new ArrayList<>(0);
        consultaTest = new Consulta(par, TipusConsulta.docsAutor);

        resultat_esperat.add(0);
        resultat_rebut.add(consultaTest.checkNumParametres(par, TipusConsulta.autorsPrefix));

        par.add("Par1");
        resultat_esperat.add(1);
        resultat_rebut.add(consultaTest.checkNumParametres(par, TipusConsulta.docsAutor));

        par.add("Par2");
        par.add("Par3");
        par.add("Par4");
        resultat_esperat.add(2);
        resultat_rebut.add(consultaTest.checkNumParametres(par, TipusConsulta.docsKSemblants));

        par.add("Par3");
        par.add("Par4");
        par.add("Par5");
        resultat_esperat.add(0);
        resultat_rebut.add(consultaTest.checkNumParametres(par, TipusConsulta.docsPC));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void testEquals() throws ExceptionConsultaLimitParametres {
        ArrayList<String> par = new ArrayList<>(0);
        par.add("Hola");
        Consulta c1 = new Consulta(par, TipusConsulta.docsKSemblants);
        par.add("Adeu");
        Consulta c2 = new Consulta(par, TipusConsulta.docsKSemblants);
        assertNotEquals(c1, c2);
    }
}
