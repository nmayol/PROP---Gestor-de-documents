package persistencia.accesObjects;

import utils.TipusConsulta;

import java.io.Serializable;
import java.util.ArrayList;

/** Representa una instancia de la classe Consulta per guardar a disc. Implementa la interficie Serializable per poder guardar-la a disc.
 *
 * @see domini.Consulta
 */
public class ConsultaAO implements Serializable {
    private ArrayList<String> parametres;
    private TipusConsulta tipusConsulta;

    public ConsultaAO(ArrayList<String> parametres, TipusConsulta tipusConsulta) {
        this.parametres = parametres;
        this.tipusConsulta = tipusConsulta;
    }

    public ArrayList<String> getParametres() {
        return parametres;
    }

    public void setParametres(ArrayList<String> parametres) {
        this.parametres = parametres;
    }

    public TipusConsulta getTipusConsulta() {
        return tipusConsulta;
    }

    public void setTipusConsulta(TipusConsulta tipusConsulta) {
        this.tipusConsulta = tipusConsulta;
    }

    @Override
    public String toString() {
        return "" +
                "Tipus:" + tipusConsulta +
                ", parametres:" + parametres;
    }
}
