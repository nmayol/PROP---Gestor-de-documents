package test.domini;

import Exceptions.ExceptionFormatNoValid;
import Exceptions.ExceptionNotPrimaryKeys;
import domini.*;
import static org.junit.Assert.*;

import org.junit.Test;
import utils.Format;

import java.util.ArrayList;
import java.util.HashMap;


public class TestDocument {

    ArrayList<String> contingut;
    Document d;
    Document res_obtindre;
    HashMap<String, Double> map_obtindre;

    @Test
    public void creadora_Document() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();

        d = new Document(Format.txt, "la ultima cena", "joan", contingut);
        assertEquals(d.getFormat(), Format.txt);
        assertEquals(d.getAutor(), "joan");
        assertEquals(d.getTitol(), "la ultima cena");
        assertEquals(d.getContingut(), new ArrayList<String>());
    }
    @Test(expected = ExceptionFormatNoValid.class)
    public void creadora_formatInvalid() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();
        d = new Document(null, null, null, contingut);
    }
    @Test(expected = ExceptionNotPrimaryKeys.class)
    public void creadora_notprimaryKey() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();
        d = new Document(Format.txt, null, null, contingut);
    }
    @Test
    public void ModificarInformacioDocument() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();
        contingut.add("hola");
        contingut.add("adeu");
        d = new Document(Format.txt, "primer", "autor1", contingut);
        contingut = new ArrayList<>();
        res_obtindre = new Document(Format.txt, "nou_doc", "nou_autor", contingut);

        //CAS 1: MODIFICAR TITOL
        d.ModificarInformacioDocument("nou_doc", "autor1", contingut);
        String td = d.getTitol();
        String tr = res_obtindre.getTitol();
        assertEquals(td, tr);

        //CAS 2: MODIFICAR AUTOR
        d.ModificarInformacioDocument("nou_doc", "nou_autor", contingut);
        String ad = d.getAutor();
        String ar = res_obtindre.getAutor();
        assertEquals(ad, ar);

        //CAS 3: MODIFICAR CONTINGUT
        d.ModificarInformacioDocument("nou_doc", "autor1", contingut);
        ArrayList<String> cd = d.getContingut();
        ArrayList<String> cr = res_obtindre.getContingut();
        assertEquals(cd, cr);

        //CAS 4: MODIFICAR TOT
        d.ModificarInformacioDocument("nou_doc", "nou_autor", contingut);
        td = d.getTitol();
        tr = res_obtindre.getTitol();
        ad = d.getAutor();
        ar = res_obtindre.getAutor();
        cd = d.getContingut();
        cr = res_obtindre.getContingut();
        assertEquals(tr, td);
        assertEquals(ar, ad);
        assertEquals(cr, cd);
    }
    @Test(expected = ExceptionNotPrimaryKeys.class)
    public void modificar_notprimaryKey() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();
        contingut.add("hola");
        contingut.add("adeu");
        d = new Document(Format.txt, "primer", "autor1", contingut);

        d.ModificarInformacioDocument(null, null, contingut);
    }
    @Test
    public void actualitzar_pesos() throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        contingut = new ArrayList<>();
        contingut.add("hola");
        contingut.add("adeu");
        contingut.add("cami");
        contingut.add("pilota");
        contingut.add("fill");
        contingut.add("adeu");
        contingut.add("desti");
        d = new Document(Format.txt, "primer", "autor1", contingut);

        map_obtindre = new HashMap<>();
        map_obtindre.put("hola", (double) 1/7);
        map_obtindre.put("adeu", (double) 2/7);
        map_obtindre.put("cami", (double) 1/7);
        map_obtindre.put("pilota", (double) 1/7);
        map_obtindre.put("fill", (double) 1/7);
        map_obtindre.put("desti", (double) 1/7);

        // CAS1: INICIALITZACIO CORRECTE
        //no cal cridar a la funcio perque ja es fa quan creem el document
        HashMap<String, Double> res_obtinguda = d.getPesosLocals();
        assertEquals(map_obtindre, res_obtinguda);

        // CAS2: MODIFIQUEM CONTINGUT AFEGINT PARAULES REPETIDES I PARAULES NOVES
        contingut.add("desti");
        contingut.add("perla");
        contingut.add("llum");
        contingut.add("desti");
        d = new Document(Format.txt, "primer", "autor1", contingut);

        map_obtindre.replace("hola", (double) 1/11);
        map_obtindre.replace("adeu", (double) 2/11);
        map_obtindre.replace("cami", (double) 1/11);
        map_obtindre.replace("pilota", (double) 1/11);
        map_obtindre.replace("fill", (double) 1/11);
        map_obtindre.replace("desti", (double) 3/11);
        map_obtindre.put("perla", (double) 1/11);
        map_obtindre.put("llum", (double) 1/11);

        res_obtinguda = d.getPesosLocals();
        assertEquals(map_obtindre, res_obtinguda);

        // CAS3 TREIEM PARAULES
        contingut.remove("desti");
        contingut.remove("desti");
        contingut.remove("hola");
        d = new Document(Format.txt, "primer", "autor1", contingut);

        map_obtindre.remove("hola");
        map_obtindre.replace("adeu", (double) 2/8);
        map_obtindre.replace("cami", (double) 1/8);
        map_obtindre.replace("pilota", (double) 1/8);
        map_obtindre.replace("fill", (double) 1/8);
        map_obtindre.replace("desti", (double) 1/8);
        map_obtindre.replace("perla", (double) 1/8);
        map_obtindre.replace("llum", (double) 1/8);

        res_obtinguda = d.getPesosLocals();
        assertEquals(map_obtindre, res_obtinguda);

        // CAS4: CONTINGUT NULL O BUIT
        contingut = new ArrayList<>();
        d = new Document(Format.txt, "primer", "autor1", contingut);
        map_obtindre = new HashMap<>();

        res_obtinguda = d.getPesosLocals();
        assertEquals(map_obtindre, res_obtinguda);

        // CAS5: CONTINGUT AMB TOTES PARAULES IGUALS
        contingut.add("hola");
        contingut.add("hola");
        contingut.add("hola");
        contingut.add("hola");
        d = new Document(Format.txt, "primer", "autor1", contingut);

        map_obtindre.put("hola", (double) 4/4);

        res_obtinguda = d.getPesosLocals();
        assertEquals(map_obtindre, res_obtinguda);
    }

}