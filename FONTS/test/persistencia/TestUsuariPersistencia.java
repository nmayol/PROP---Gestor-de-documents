package test.persistencia;

import Exceptions.ExceptionDirectoriNoCreat;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionUsuariJaExisteix;
import org.junit.Before;
import org.junit.Test;
import persistencia.UsuariPersistencia;
import utils.PairP;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestUsuariPersistencia {

    UsuariPersistencia userP;

    @Before
    public void before(){
        userP= new UsuariPersistencia("/EXE/test/persistencia/usuaris");
    }

    /**
     * Només cal comprovar que salta correctament l'excepció si l'usuari ja existeix.
     * La resta son tot funcions de IOFitxers, ja testejades.
     */
    @Test
    public void testNouUsuari() {

    }

    /**
     * Només cal comprovar que salta correctament l'excepció si no existeix l'usuari.
     * A part d'això només es fa una crida a IOFitxers, que ja està testejat.
     */
    @Test
    public void testEliminarUsuari() {
    }

    @Test
    public void testIniciarSessio() {
    }

    @Test
    public void testTancarSessio() {
    }

    @Test
    public void testEliminarDoc() {
    }

    @Test
    public void testGuardarDoc() {
    }

    @Test
    public void testGetLlistaDocsUsuari() {
    }

    @Test
    public void testDocUtilitzat() {
    }
}
