package persistencia.accesObjects;

import utils.Format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/** Representa una instancia de la classe Document per guardar a disc. Implementa la interficie Serializable per poder guardar-la a disc.
 *
 * @see domini.Document
 */
public class DocumentAO implements Serializable {
    private Format format;
    private String titol;
    private String autor;
    private HashMap<String, Double> ParaulaPesLocal;
    private ArrayList<String> contingut;
    private double norma;

    public DocumentAO(Format format, String titol, String autor, HashMap<String, Double> paraulaPesLocal, ArrayList<String> contingut, double norma) {
        this.format = format;
        this.titol = titol;
        this.autor = autor;
        ParaulaPesLocal = paraulaPesLocal;
        this.contingut = contingut;
        this.norma = norma;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public HashMap<String, Double> getParaulaPesLocal() {
        return ParaulaPesLocal;
    }

    public void setParaulaPesLocal(HashMap<String, Double> paraulaPesLocal) {
        ParaulaPesLocal = paraulaPesLocal;
    }

    public ArrayList<String> getContingut() {
        return contingut;
    }

    public void setContingut(ArrayList<String> contingut) {
        this.contingut = contingut;
    }

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
    }

    @Override
    public String toString() {
        return "titol:" + titol +
                ", autor:" + autor;
    }
}
