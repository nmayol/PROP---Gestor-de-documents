package test.persistencia;

import domini.Document;
import org.junit.Before;
import org.junit.Test;
import persistencia.DocumentPersistencia;
import utils.Format;

public class TestDocumentPersistencia {

    DocumentPersistencia documentPersistencia;

    @Before
    public void before(){
        documentPersistencia = new DocumentPersistencia("EXE/test/persistencia/documents");
    }
    @Test
    public void testGuardarDocument(){
    }

    @Test
    public void testGetDocument(){
    }

    @Test
    public void testGetDocuments(){
    }

    @Test
    public void testEliminarDocument(){
    }
}
