package test.domini.controladors;

import Exceptions.ExceptionConsultaLimitParametres;
import Exceptions.ExceptionIndexConsultaNoValid;
import domini.Consulta;
import domini.Controladors.CtrlConsulta;
import utils.TipusConsulta;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class TestCtrlConsulta {
    CtrlConsulta ctrlConsulta;

    @Test
    public void testNovaConsulta() throws ExceptionConsultaLimitParametres {
        ArrayList<Consulta> resultat_esperat = new ArrayList<>();
        ArrayList<Consulta> resultat_rebut = new ArrayList<>();
        ctrlConsulta = new CtrlConsulta();

        ArrayList<String> par = new ArrayList<>();

        // CAS 1 AFEGIR NORMAL
        par.add("a");
        Consulta c = new Consulta(par, TipusConsulta.docsAutor);
        ctrlConsulta.NovaConsulta(c);
        resultat_esperat.add(0, c);
        par = new ArrayList<>();
        par.add("c1");
        Consulta c1 = new Consulta(par, TipusConsulta.docsAutor);
        ctrlConsulta.NovaConsulta(c1);
        resultat_esperat.add(0, c1);

        resultat_rebut = ctrlConsulta.getHistorialTipus(TipusConsulta.docsKSemblants);
        assertEquals(resultat_esperat, resultat_rebut);

        // JA HI ERA -> ACTUALITZAR
        ctrlConsulta.NovaConsulta(c);
        resultat_esperat.remove(c);
        resultat_esperat.add(0, c);

        assertEquals(resultat_esperat, resultat_rebut);

        // CAS 3 MAXSIZE
        ctrlConsulta.EsborrarConsultes();
        resultat_esperat.clear();
        //ctrlConsulta.NovaConsulta(c);
        // i < MAXSIZE
        for (int i = 0; i < 6; i++) {
            par = new ArrayList<>();
            par.add(Integer.toString(i));
            Consulta cf = new Consulta(par, TipusConsulta.docsAutor);
            ctrlConsulta.NovaConsulta(cf);
            if (i > 0) resultat_esperat.add(0, cf);
        }
        ctrlConsulta.NovaConsulta(c1);
        resultat_esperat.add(0, c1);
        resultat_rebut = ctrlConsulta.getHistorialTipus(TipusConsulta.docsAutor);
        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void getHistorialTipus() throws ExceptionConsultaLimitParametres {
        ArrayList<Consulta> resultat_esperat = new ArrayList<>();
        ArrayList<Consulta> resultat_rebut = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ctrlConsulta = new CtrlConsulta();

        Consulta c1 = new Consulta(par, TipusConsulta.docsAutor);
        ctrlConsulta.NovaConsulta(c1);
        resultat_esperat.add(c1);
        Consulta c2 = new Consulta(par, TipusConsulta.docsKSemblants);
        ctrlConsulta.NovaConsulta(c2);


        assertEquals(resultat_esperat, ctrlConsulta.getHistorialTipus(TipusConsulta.docsAutor));
        resultat_esperat.clear();
        resultat_esperat.add(c2);
        assertEquals(resultat_esperat, ctrlConsulta.getHistorialTipus(TipusConsulta.docsPC));
    }

    @Test
    public void testGetConsulta() throws ExceptionIndexConsultaNoValid, ExceptionConsultaLimitParametres {
        ArrayList<String> par = new ArrayList<>();
        ctrlConsulta = new CtrlConsulta();

        Consulta c = new Consulta(par, TipusConsulta.docsAutor);
        ctrlConsulta.NovaConsulta(c);

        par.add("Par");
        Consulta c1 = new Consulta(par, TipusConsulta.docsAutor);
        ctrlConsulta.NovaConsulta(c1);

        Consulta c2 = new Consulta(par, TipusConsulta.docsPC);
        ctrlConsulta.NovaConsulta(c2);

        ArrayList<Consulta> resultat_esperat = new ArrayList<>();
        ArrayList<Consulta> resultat_rebut = new ArrayList<>();
        resultat_esperat.add(c);
        resultat_rebut.add(ctrlConsulta.getConsulta(c.getTipus(), 2));
        resultat_esperat.add(c1);
        resultat_rebut.add(ctrlConsulta.getConsulta(c1.getTipus(), 1));
        resultat_esperat.add(c2);
        resultat_rebut.add(ctrlConsulta.getConsulta(c2.getTipus(), 1));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void testEsborrarConsulta() throws ExceptionIndexConsultaNoValid, ExceptionConsultaLimitParametres {
        ArrayList<Consulta> resultat_esperat = new ArrayList<>();
        ArrayList<Consulta> resultat_rebut = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ctrlConsulta = new CtrlConsulta();

        Consulta c1 = new Consulta(par, TipusConsulta.docsAutor);
        resultat_esperat.add(c1);
        ctrlConsulta.NovaConsulta(c1);

        Consulta c2 = new Consulta(par, TipusConsulta.docsPC);
        ctrlConsulta.NovaConsulta(c2);
        ctrlConsulta.esborrarConsulta(c2.getTipus(), 1);

        resultat_rebut = ctrlConsulta.getConsultes();
        assertEquals(resultat_esperat, resultat_rebut);
    }
}