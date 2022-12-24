package domini;

import Exceptions.ExceptionConsultaLimitParametres;
import Exceptions.ExceptionDirectoriNoEliminat;
import utils.TipusConsulta;

import java.io.Serializable;
import java.util.*;

/** Representa una consulta a l'aplicacio. Les consultes formen els historials.
 * @author tomas.calaf.marti
 */
public class Consulta {
    private ArrayList<String> parametres;
    private TipusConsulta tipus;

    /** Constructora
     * @throws  ExceptionConsultaLimitParametres La quantitat de parametres sobrepasa el limit per al seu tipus
     * @see TipusConsulta tipus consulta
     * @param parametres parametres
     * @param tipus tipus consulta
     * @throws ExceptionConsultaLimitParametres salta quan la consulta supera el limit de parametres
     */
    public Consulta(ArrayList<String> parametres, TipusConsulta tipus) throws ExceptionConsultaLimitParametres {
        this.parametres = new ArrayList<>(0);
        if(checkNumParametres(parametres, tipus) < 2) this.parametres.addAll(parametres);
        else throw new ExceptionConsultaLimitParametres();
        this.tipus = tipus;
    }

    /** Insereix un nou parametre si es possible a la consulta.
     * @param par parametre
     * @throws ExceptionConsultaLimitParametres No caben mes parametres
     */
    public void nouParametre(String par) throws ExceptionConsultaLimitParametres {
        if(checkNumParametres(parametres, tipus) == 0) parametres.add(par);
        else throw new ExceptionConsultaLimitParametres();
    }

    /** Retorna la consulta en forma d'expressio
     * @return string que s'ha de veure en pantalla en mostrar l'historial
     */
    @Override
    public String toString(){
        String e = "";
        switch (tipus){
            case autorsPrefix:
                e = "Prefix: " + parametres.toString();
                break;
            case docsAutor:
                e = "Autor: " + parametres.toString();
                break;
            case docsKSemblants:
                e = "Autor, titol, numero: " + parametres.toString();
                break;
            case docsPC:
                e = "Paraules clau: " + parametres.toString();
                break;
            default:
                break;
        }
        return e;
    }

    /** Comprova la mida d'una llista de parametres per un cert tipus d'expressio.
     * @param par llista de parametres
     * @param tip tipus consulta
     * @see TipusConsulta
     * @return retorna num de parametres
     */
    public int checkNumParametres(ArrayList<String> par, TipusConsulta tip) {
        switch (tip){
            case autorsPrefix:
            case docsAutor:
                if(par.size() < 1) return 0;
                else if(par.size() == 1) return 1;
                else return 2;
            case docsKSemblants:
                if(par.size() < 3) return 0;
                else if(par.size() == 3) return 1;
                else return 2;
            default:
                return 0;
        }
    }

    /** Retorna una llista amb els parametres de la consulta
     *
     * @return Llista de Strings
     */
    public ArrayList<String> getParametres() {
        return parametres;
    }

    /** Assigna els parametres a la consulta
     *
     * @param parametres Llista de parametres
     */
    public void setParametres(ArrayList<String> parametres) {
        this.parametres = parametres;
    }

    /** Retorna el tipus de la consulta
     *
     * @return TipusConsulta
     * @see TipusConsulta
     */
    public TipusConsulta getTipus() {
        return tipus;
    }

    /** Assigna els tipus a la consulta
     *
     * @param tipus Tipus de consulta.
     * @see TipusConsulta
     */
    public void setTipus(TipusConsulta tipus) {
        this.tipus = tipus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(consulta.parametres, parametres) && consulta.tipus == tipus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parametres, tipus);
    }
}