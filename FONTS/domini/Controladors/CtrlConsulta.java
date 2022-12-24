package domini.Controladors;

import Exceptions.ExceptionIndexConsultaNoValid;
import domini.*;
import utils.TipusConsulta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CtrlConsulta {

    private static Map<TipusConsulta, ArrayList<Consulta>> TotalConsultes;
    private static final int MAXSIZE = 6;

    /** Constructora.
     *
     */
    public CtrlConsulta() {
        TotalConsultes = new HashMap<>();
        TotalConsultes.put(TipusConsulta.docsAutor, new ArrayList<>());
        TotalConsultes.put(TipusConsulta.docsKSemblants, new ArrayList<>());
        TotalConsultes.put(TipusConsulta.docsPC, new ArrayList<>());
        TotalConsultes.put(TipusConsulta.autorsPrefix, new ArrayList<>());
    }

    /** Afegeix una nova consulta. Actualitza l'historial pertinent.
     *
     * @param consulta Nova consulta.
     */
    public void NovaConsulta(Consulta consulta) {
        if (!TotalConsultes.containsKey(consulta.getTipus())) TotalConsultes.put(consulta.getTipus(), new ArrayList<>(0));
        // Checks whether the search c is in Historial or not. If yes erases the queue element.
        TotalConsultes.get(consulta.getTipus()).remove(consulta);

        // If Historial size is MAXSIZE, last searched element is erased
        if (TotalConsultes.get(consulta.getTipus()).size() == MAXSIZE) TotalConsultes.get(consulta.getTipus()).remove(MAXSIZE - 1);

        // Adds the last search to Historial
        TotalConsultes.get(consulta.getTipus()).add(0, consulta);
    }

    /** Retorna les consultes de un historial.
     *
     * @param tipusConsulta Tipus d'historial.
     * @return Llista de consultes.
     */
    public ArrayList<Consulta> getHistorialTipus(TipusConsulta tipusConsulta) {
        return TotalConsultes.get(tipusConsulta);
    }

    /** Retorna totes les consultes de l'aplicacio, de tots els historials.
     *
     * @return Llista de consultes
     */
    public ArrayList<Consulta> getConsultes() {
        ArrayList<Consulta> l = new ArrayList<>();
        TotalConsultes.values().forEach(l::addAll);
        return l;
    }

    /** Esborra totes les consultes de l'aplicacio.
     *
     */
    public void EsborrarConsultes() {
        TotalConsultes.values().forEach(ArrayList::clear);
    }

    /** Esborra la k consulta mes antiga de l'historial del tipus especificat.
     *
     * @param tipusConsulta Tipus d'historial.
     * @param k Ordre d'antiguitat {1, MAXSIZE}
     * @throws ExceptionIndexConsultaNoValid L'enter aportat no es valid.
     */
    public void esborrarConsulta(TipusConsulta tipusConsulta, int k) throws ExceptionIndexConsultaNoValid {
        if(k < 1 | k > TotalConsultes.size()) throw new ExceptionIndexConsultaNoValid(k);
        TotalConsultes.get(tipusConsulta).remove(k - 1);
    }

    /** Retorna la k consulta mes antiga de l'historial del tipus especificat.
     *
     * @param tipusConsulta Tipus d'historial.
     * @param k Ordre d'antiguitat {1, MAXSIZE}
     * @return Consulta
     * @throws ExceptionIndexConsultaNoValid L'enter aportat no es valid.
     */
    public Consulta getConsulta(TipusConsulta tipusConsulta, int k) throws ExceptionIndexConsultaNoValid {
        ArrayList<Consulta> consultes = TotalConsultes.get(tipusConsulta);
        if(k < 1 | k > consultes.size()) throw new ExceptionIndexConsultaNoValid(k);
        return consultes.get(k - 1);
    }

    /** Assigna una llista de consultes a un historial especific. Esborra les consultes antigues d'quest historial.
     * Les consultes de la llista han de ser totes del tipus correcte.
     *
     * @param tipusConsulta Tipus d'historial.
     * @param consultes Llista de consultes.
     */
    public void setConsultesTipus(TipusConsulta tipusConsulta, ArrayList<Consulta> consultes){
        TotalConsultes.remove(tipusConsulta);
        TotalConsultes.put(tipusConsulta, consultes);
    }

    /** Assigna les consultes d'una llista als historials pertinents en cada cas, en ordre. Esborra tots els historials antics.
     * Les consultes de la llista poden ser del tipus que sigui.
     *
     * @param consultes Llista de consultes.
     */
    public void setConsultes(ArrayList<Consulta> consultes){
        EsborrarConsultes();
        TotalConsultes.put(TipusConsulta.docsAutor, new ArrayList<>(0));
        TotalConsultes.put(TipusConsulta.docsKSemblants, new ArrayList<>(0));
        TotalConsultes.put(TipusConsulta.docsPC, new ArrayList<>(0));
        TotalConsultes.put(TipusConsulta.autorsPrefix, new ArrayList<>(0));

        consultes.forEach(consulta -> TotalConsultes.get(consulta.getTipus()).add(consulta));
    }
}
