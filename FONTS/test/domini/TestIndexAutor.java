package test.domini;

import Exceptions.ExceptionAutorNoExisteix;
import Exceptions.ExceptionAutorJaExisteix;
import Exceptions.ExceptionPrefixNoExisteix;
import domini.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestIndexAutor {

    @Test
    public void afegirautors() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String> autors_esperats = new ArrayList<>();
        autors_esperats.add("joan");
        autors_esperats.add("neus");
        autors_esperats.add("paula");
        autors_esperats.add("tomas");

        ArrayList<String> autors_resultants = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("joan");
        autors.afegirAutor("paula");
        autors.afegirAutor("tomas");
        autors.afegirAutor("neus");

        autors_resultants.addAll(autors.cercarAutors(""));

        assertEquals(autors_esperats, autors_resultants);
    }

    @Test(expected = ExceptionAutorJaExisteix.class)
    public void afegirautors_autorjaexisteix() throws ExceptionAutorJaExisteix {
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("neus");
        autors.afegirAutor("neus");
    }

    @Test(expected = ExceptionAutorNoExisteix.class)
    public void cercarautors_autornoexisteix() throws ExceptionAutorJaExisteix, ExceptionPrefixNoExisteix {
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("neus");
        ArrayList<String> resultat = new ArrayList<>();
        resultat.addAll(autors.cercarAutors("j"));
    }

    @Test
    public void afegirautors_sempre() throws ExceptionPrefixNoExisteix {
        ArrayList<String> autors_esperats = new ArrayList<>();
        autors_esperats.add("paula");

        ArrayList<String> autors_resultants = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor_sempre("paula");
        autors_resultants.addAll(autors.cercarAutors(""));

        assertEquals(autors_esperats, autors_esperats);
    }

    @Test
    public void eliminarautors1() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("joan");
        resultat_esperat.add("neus");
        resultat_esperat.add("paula");
        resultat_esperat.add("tomas");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("joan");
        autors.afegirAutor("paula");
        autors.afegirAutor("pau");
        autors.afegirAutor("laura");
        autors.eliminarAutor("pau");
        autors.afegirAutor("tomas");
        autors.afegirAutor("neus");
        autors.eliminarAutor("laura");
        resultat_rebut.addAll(autors.cercarAutors(""));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void eliminarautors2() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("palau");
        resultat_esperat.add("paula");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("pau");
        autors.afegirAutor("paula");
        autors.afegirAutor("palau");
        autors.eliminarAutor("pau");
        resultat_rebut.addAll(autors.cercarAutors(""));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void eliminarautors3() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("palau");
        resultat_esperat.add("pau");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("pau");
        autors.afegirAutor("paula");
        autors.afegirAutor("palau");
        autors.eliminarAutor("paula");
        resultat_rebut.addAll(autors.cercarAutors(""));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test
    public void canviautor() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix, ExceptionPrefixNoExisteix {
        ArrayList<String> resultat_esperat = new ArrayList<>();
        resultat_esperat.add("joan");

        ArrayList<String> resultat_rebut = new ArrayList<>();
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("paula");
        autors.canvi_Autor("paula", "joan");
        resultat_rebut.addAll(autors.cercarAutors(""));

        assertEquals(resultat_esperat, resultat_rebut);
    }

    @Test(expected = ExceptionAutorNoExisteix.class)
    public void canviautor_autoranticnoexisteix() throws ExceptionAutorNoExisteix, ExceptionAutorJaExisteix {
        IndexAutor autors = new IndexAutor();
        autors.canvi_Autor("antic", "nou");
    }

    @Test(expected = ExceptionAutorJaExisteix.class)
    public void canviautor_autornoujaexisteix() throws ExceptionAutorJaExisteix, ExceptionAutorNoExisteix {
        IndexAutor autors = new IndexAutor();
        autors.afegirAutor("neus");
        autors.afegirAutor("tomas");
        autors.canvi_Autor("tomas", "neus");
    }

}
