package test.persistencia;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoCreat;
import Exceptions.ExceptionDirectoriNoEliminat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistencia.IOFitxers;
import test.persistencia.stubs.ObjecteStub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.*;

public class TestIOFitxers {
    private static IOFitxers ioFitxers = null;
    private static String path;

    private static File dirTest;

    @Before
    public void before(){
        ioFitxers = new IOFitxers();
        path = "EXE/persistencia/test";
        dirTest = new File(path + "/dir");
    }

    @After
    public void after() throws ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, IOException {
        ioFitxers.clearDirectori(dirTest);
        ioFitxers.escriureFitxer(path + "/test.txt", "Linia 1\nLinia 2", false);
    }

    @Test
    public void testCrearDirectori() throws ExceptionDirectoriNoCreat {
        File file = new File(dirTest + "/test1/test2");
        ioFitxers.crearDirectori(dirTest + "/test1/test2");
        assertTrue(file.exists());
    }

    @Test
    public void testEliminarDirectori() throws ExceptionDirectoriNoCreat, ExceptionDirectoriNoEliminat {
        File file = new File(dirTest + "/test");
        ioFitxers.crearDirectori(dirTest + "/test");

        ioFitxers.eliminarDirectori(dirTest + "/test");

        assertFalse(file.exists());
    }

    @Test
    public void testClearDirectori() throws ExceptionDirectoriNoCreat, ExceptionDirectoriNoEliminat {
        ioFitxers.crearDirectori(dirTest + "/test1");
        ioFitxers.crearDirectori(dirTest + "/testDirectori/test2");
        ioFitxers.crearDirectori(dirTest + "/testDirectori/test2/test");
        ioFitxers.crearDirectori(dirTest + "/testDirectori/test2/test.txt");

        ioFitxers.clearDirectori(dirTest);
        assertEquals(0, Objects.requireNonNull(dirTest.listFiles()).length);
    }

    @Test
    public void testEscriureObjecte() throws ExceptionDirNoTrobat, IOException {
        File file = new File(dirTest + "/object.dat");
        ioFitxers.escriureObjecte(new ObjecteStub(1), dirTest + "/object.dat");

        assertTrue(file.exists());
    }

    @Test
    public void testLlegirObjecte() throws ExceptionDirNoTrobat, IOException, ClassNotFoundException {
        ObjecteStub objecteStub = new ObjecteStub(1);
        ioFitxers.escriureObjecte(objecteStub, dirTest + "/object.dat");

        ObjecteStub o = (ObjecteStub) ioFitxers.llegirObjecte(dirTest + "/object.dat");
        assertEquals(o, objecteStub);
    }

    @Test
    public void testLlegirFitxer() throws FileNotFoundException {
        String text = ioFitxers.llegirfitxer(path + "/test.txt");
        assertEquals("Linia 1\nLinia 2\n", text);
    }

    @Test
    public void testEscriureFitxer() throws IOException, ExceptionDirNoTrobat {
        ioFitxers.escriureFitxer(path + "/test.txt", "Test", false);

        assertEquals("Test\n", ioFitxers.llegirfitxer(path + "/test.txt"));
    }
}
