package test.domini;

import Exceptions.ExceptionAutorNoExisteix;
import Exceptions.ExceptionDocumentJaExisteix;
import Exceptions.ExceptionDocumentNoExisteix;
import domini.IndexDocumentsAutor;
import org.junit.Test;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TestIndexDocumentsAutor {

    @Test
    public void afegeixTitolAutor() throws ExceptionAutorNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("cjt_documents");

        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("joan", "cjt_documents");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        resultat_rebut.addAll(autils.buscarTitolsAutor("joan"));

        assertEquals(resultat_esperat, resultat_rebut);

        autils.afegeixTitolAutor("joan", "document");
        resultat_rebut.clear();
        resultat_rebut.addAll(autils.buscarTitolsAutor("joan"));

        resultat_esperat.clear();
        resultat_esperat.add("document");
        resultat_esperat.add("cjt_documents");

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void eliminarTitolAutor() throws ExceptionAutorNoExisteix, ExceptionDocumentNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("expressions_booleanes");

        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("joan", "cjt_documents");
        autils.afegeixTitolAutor("neus", "expressions_booleanes");
        autils.eliminarTitolAutor("joan", "cjt_documents");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        resultat_rebut.addAll(autils.buscarTitolsAutor("neus"));

        assertEquals(resultat_esperat, resultat_rebut);

        //////////////////////////////////////////////////////

        resultat_esperat.clear();
        resultat_esperat.add("index_expressio_booleana");

        autils.afegeixTitolAutor("neus", "index_expressio_booleana");
        autils.eliminarTitolAutor("neus", "expressions_booleanes");

        resultat_rebut.clear();
        resultat_rebut.addAll(autils.buscarTitolsAutor("neus"));
        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test(expected = ExceptionAutorNoExisteix.class)
    public void eliminarTitolAutor_autornoexisteix() throws ExceptionDocumentNoExisteix, ExceptionAutorNoExisteix {
        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("joan", "cjt_documents");
        autils.eliminarTitolAutor("paula", "mandarines");
    }

    @Test(expected = ExceptionDocumentNoExisteix.class)
    public void eliminarTitolAutor_docnoexisteix() throws ExceptionDocumentNoExisteix, ExceptionAutorNoExisteix {
        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("joan", "cjt_documents");
        autils.eliminarTitolAutor("joan", "mandarines");
    }

    @Test
    public void llistarTitolsAutor() throws ExceptionAutorNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("titola");
        resultat_esperat.add("titolb");
        resultat_esperat.add("titolc");
        resultat_esperat.add("titold");

        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("autor", "titola");
        autils.afegeixTitolAutor("autor", "titolb");
        autils.afegeixTitolAutor("autor", "titolc");
        autils.afegeixTitolAutor("autor", "titold");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        resultat_rebut.addAll(autils.buscarTitolsAutor("autor"));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test(expected = ExceptionAutorNoExisteix.class)
    public void llistarTitolsAutor_autornoexisteix() throws ExceptionAutorNoExisteix {
        IndexDocumentsAutor autils = new IndexDocumentsAutor();
        autils.afegeixTitolAutor("joan", "cjt_documents");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        resultat_rebut.addAll(autils.buscarTitolsAutor("paula"));

        /////////

        IndexDocumentsAutor autils2 = new IndexDocumentsAutor();
        ArrayList<String> resultat_rebut2 = new ArrayList<>();
        resultat_rebut2.addAll(autils2.buscarTitolsAutor("autor"));
    }

}
