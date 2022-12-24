package test.domini.controladors;

import Exceptions.*;
import domini.Controladors.CtrlUsuari;
import domini.Usuari;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestCtrlUsuari {
    CtrlUsuari ctrlUsuariTest;

    @Before
    public void beforeClass() throws Exception {
        ctrlUsuariTest = new CtrlUsuari();
    }

    @Test
    public void numeroUsuarisTest() {
        assertNotNull(ctrlUsuariTest);
        assertEquals(0, ctrlUsuariTest.numeroUsuaris());
    }
    @Test(expected = ExceptionUsuariJaExisteix.class)
    public void afegirUsuariTest() throws ExceptionUsuariJaExisteix {
        ArrayList<Usuari> resultat_esperat = new ArrayList<>();
        ArrayList<Usuari> resultat_rebut = new ArrayList<>();

        Usuari u1 = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u1);
        resultat_esperat.add(u1);

        Usuari u2 = new Usuari("Tomas", "111");
        ctrlUsuariTest.afegirUsuari(u1);

        Usuari u3 = new Usuari("Paula", "123");
        ctrlUsuariTest.afegirUsuari(u1);
        resultat_esperat.add(u3);

        resultat_rebut = ctrlUsuariTest.getUsuaris();
        assertEquals(resultat_esperat, resultat_rebut);
    }
    @Test(expected = ExceptionContrasenyaIncorrecta.class)
    public void eliminarUsuariTest() throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta {
        ArrayList<Usuari> resultat_esperat = new ArrayList<>();
        ArrayList<Usuari> resultat_rebut = new ArrayList<>();

        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);

        Usuari u1 = new Usuari("Paula", "123");
        ctrlUsuariTest.afegirUsuari(u1);
        resultat_esperat.add(u1);

        ctrlUsuariTest.eliminarUsuari(u.getNom(), u.getContrasenya());
        ctrlUsuariTest.eliminarUsuari(u1.getNom(), "1");

        resultat_rebut = ctrlUsuariTest.getUsuaris();
        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void iniciarSessioTest() throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionNoSessioActiva {
        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);

        ctrlUsuariTest.iniciarSessio("Tomas", "123");
        assertEquals(u, ctrlUsuariTest.getUsuariActiu());
    }

    @Test(expected = ExceptionUsuariNoExisteix.class)
    public void iniciarSessioUsuariNoExisteix() throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionNoSessioActiva {
        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);

        ctrlUsuariTest.iniciarSessio("Toma", "123");
        assertNull(ctrlUsuariTest.getUsuariActiu());
    }

    @Test(expected = ExceptionContrasenyaIncorrecta.class)
    public void iniciarSessioContraIncorrecta() throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionNoSessioActiva {
        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);

        ctrlUsuariTest.iniciarSessio("Tomas", "12");
        assertNull(ctrlUsuariTest.getUsuariActiu());
    }

    @Test(expected = ExceptionSessioJaActiva.class)
    public void iniciarSessioJaActiva() throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionNoSessioActiva {
        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);
        Usuari u1 = new Usuari("Paula", "123");
        ctrlUsuariTest.afegirUsuari(u1);

        ctrlUsuariTest.iniciarSessio("Tomas", "123");
        ctrlUsuariTest.iniciarSessio("Paula", "123");

        assertEquals(u, ctrlUsuariTest.getUsuariActiu());
    }

    @Test(expected = ExceptionNoSessioActiva.class)
    public void tancarSessioTest()
            throws ExceptionUsuariJaExisteix, ExceptionUsuariNoExisteix, ExceptionSessioJaActiva, ExceptionContrasenyaIncorrecta, ExceptionNoSessioActiva {
        Usuari u = new Usuari("Tomas", "123");
        ctrlUsuariTest.afegirUsuari(u);
        ctrlUsuariTest.iniciarSessio("Tomas", "123");

        ctrlUsuariTest.tancarSessio();
        ctrlUsuariTest.tancarSessio();

        assertNull(ctrlUsuariTest.getUsuariActiu());
    }
}