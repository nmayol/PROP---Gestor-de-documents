package test.domini;

import domini.Usuari;
import org.junit.BeforeClass;

/**
 * Aquesta classe només conte dos atributs i els getters i setters. Per el que no est testeja res.
 */
public class TestUsuari {

    Usuari usuariTest;

    @BeforeClass
    public void beforeClass() {
        usuariTest = new Usuari("Tomas", "123");
    }
}
