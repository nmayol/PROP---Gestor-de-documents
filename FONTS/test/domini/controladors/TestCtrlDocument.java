package test.domini.controladors;


import Exceptions.ExceptionDocumentNoExisteix;
import domini.Controladors.CtrlDocument;
import domini.Document;

import org.junit.Test;
import utils.Format;
import utils.KeyP;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCtrlDocument {
    /* ELIMINAR DOCUMENT
    - eliminar document que existeix
    - eliminar document que no existeix
    - eliminar document amb claus erroneas
    */
    /*@Test
    public void EliminaDocument() throws IOException, ExceptionDocumentNoExisteix {
        Path fileName = Path.of("C:/Users/Joan/Documents/UPC_GEI/PROP/empty-ca.txt");
        String s = Files.readString(fileName, StandardCharsets.UTF_8);
        String[] words = s.split("\\p{Punct}| |\\n|\\¿|\\¡");
        HashSet<String> ca_stop_words = new HashSet<String>(List.of(words));

        CtrlDocument c = new CtrlDocument();
        ArrayList<String> contingut = new ArrayList<String>();
        contingut.add("hola");
        contingut.add("adeu");
        Document d1 = new Document(Format.txt, "primer", "autor1", contingut, ca_stop_words);
        Document d2 = new Document(Format.txt, "segon", "autor2", contingut, ca_stop_words);
        Document d3 = new Document(Format.txt, "tercer", "autor3", contingut, ca_stop_words);

        HashMap<KeyP, Document> resultat_esperat = new HashMap<KeyP, Document> ();
        resultat_esperat.put(new KeyP(d2.getAutor(), d2.getTitol()), d2);
        resultat_esperat.put(new KeyP(d3.getAutor(), d3.getTitol()), d3);

        c.add_document(d1);
        c.add_document(d2);
        c.add_document(d3);

        // CAS 1: ELIMINA NOMÉS DOCUMENTS QUE EXISTEIXEN
        c.EliminarDocument("autor1", "primer");

        // CAS 2: ELIMINA DOCUMENTS QUE NO EXISTEIXEN
        // c.EliminarDocument("autor1", "primer");

        HashMap<KeyP, Document> resultat_rebut = c.getCjt_documents();
        assertEquals(resultat_esperat, resultat_rebut);
    }*/

    /*MODIFICAR INFORMACIO DOCUMENT
    - modificar document que no existeix
    - modificar diferents elements del document
     */
    @Test
    public void ModificarInformacioDocument(){}

    /*OBRIR DOCUMENT
    - obrir document que no existeix
    - obrir document que existeix
     */
    @Test
    public void ObrirDocument(){}

    /*NOU DOCUMENT*/
    @Test
    public void NouDocument(){}

    //LLISTAR DOCUMENTS SEMBLANTS
    /*  CtrlDocument c = new CtrlDocument();
        ArrayList<String> contingut = new ArrayList<String>();
        contingut.add("hola");
        contingut.add("cadira");
        contingut.add("persona");
        Document d1 = new Document(Format.txt, "primer", "autor1", contingut);
        Document d2 = new Document(Format.txt, "segon", "autor2", contingut);
        ArrayList<String> contingut3 = new ArrayList<String>();
        contingut3.add("taula");
        contingut3.add("cadira");
        contingut3.add("adeu");
        contingut3.add("persona");
        Document d3 = new Document(Format.txt, "tercer", "autor3", contingut3);
        ArrayList<String> contingut4 = new ArrayList<String>();
        contingut4.add("illa");
        Document d4 = new Document(Format.txt, "quart", "autor4", contingut4);


        c.add_document(d1);
        c.add_document(d2);
        c.add_document(d3);
        c.add_document(d4);

        c.LlistarDocumentsSemblants("autor1", "primer", 1);
     */
    @Test
    public void LlistarSemblants(){}
}
