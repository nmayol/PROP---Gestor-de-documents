package domini;
import Exceptions.*;
import utils.ExpressioBooleana;
import utils.KeyP;

import java.io.Serializable;
import java.util.*;

/**
 * Classe corresponent a l'endex de les cerques amb una esxpressio booleana.
 * @author Neus Mayol
 */
public class IndexExpressionsBooleanes implements Serializable {

    private static HashMap<ExpressioBooleana, Set<KeyP>> Index;
    private static ArrayList<String> Expressions;

    /**
     * Creadora, crea l'endex
     */
    public IndexExpressionsBooleanes() {
        super();
        Index = new HashMap<>();
        Expressions = new ArrayList<>();
    }

    public IndexExpressionsBooleanes(HashMap<ExpressioBooleana, Set<KeyP>> index) {
        Index = index;
    }

    public static void netejarIndex() {
        Index.clear();
        Expressions.clear();
    }

    /**
     * Retorna l'unic atribut de la classe.
     */
    public HashMap<ExpressioBooleana, Set<KeyP>> getIndex() {
        return Index;
    }

    public static ArrayList<String> getContingutIndex() {
        return Expressions;

    }

    /**
     * Retorna les expressions que conte l'endex.
     * @return retorna index
     */
    public Set<ExpressioBooleana> getExpressions() {
        return Index.keySet();
    }


    /**
     * Retorna els documents que compleixen una expressio.
     * @param e Expressio sobre la qual es busca una avaluacio dels documents.
     * @return Els identificadors de documents que compleixen l'expressio d'entrada.
     */
    public static ArrayList<String[]> getDocumentsExpressio(ExpressioBooleana e) {
        Set<KeyP> res = new HashSet<KeyP>();
        ArrayList<String[]> benposat =  new ArrayList<>();
        for (Map.Entry<ExpressioBooleana,Set<KeyP>> set : Index.entrySet()) {
            if (set.getKey().getArbre(set.getKey().getExpressio().getRoot()).equals(e.getArbre(e.getExpressio().getRoot())))
                res = set.getValue();
        }
        Iterator value = res.iterator();
        while (value.hasNext()) {
           KeyP val = (KeyP) value.next();
           benposat.add(new String[]{val.getK1(), val.getK2()});
        }
        return benposat;
    }

    /**
     * Retorna els documents que compleixen una expressio.
     * @param s cadena de caracters corresponent a una expressio introduïda per l'usuari.
     * @return Els identificadors de documents que compleixen l'expressio d'entrada.
     * @throws ExceptionExpressioBooleanaIncorrecta En cas que l'usuari s'hagi equivocat introduint l'expressio
     */
    public static ArrayList<String[]> getDocumentsExpressio(String s) throws ExceptionExpressioBooleanaIncorrecta {
        return getDocumentsExpressio(new ExpressioBooleana(s));
    }


    /**
     * Afegeix una nova cerca a l'endex, en cas que no existeixi ja.
     * @param expr cadena de caracters corresponent a una expressio introduïda per l'usuari.
     * @throws ExceptionExpressioBooleanaIncorrecta En cas que l'usuari s'hagi equivocat introduint l'expressio
     */
    public void afegirExpressio(String expr) throws ExceptionExpressioBooleanaIncorrecta {
        ExpressioBooleana e = new ExpressioBooleana(expr);
        Expressions.add(expr);
        boolean trobat = false;
        // Iterating every set of entry in the HashMap
        for (ExpressioBooleana it : Index.keySet()) {
            if (it.getArbre(it.getExpressio().getRoot()).equals(e.getArbre(e.getExpressio().getRoot()))) { trobat = true; }
        }
        if (!trobat) Index.put(e, new HashSet<KeyP>()) ;
    }


    /**
     * S'avaluen totes les expressions de l'endex sobre un document, i es fan les modificacions pertinents a
     * l'endex segons els resultats de les avaluacions.
     * @param d Document sobre el qual s'han d'avaluar totes les expressions.
     */
    public void evaluarTotesExpressions(Document d) {
        KeyP info = new KeyP(d.getTitol(),d.getAutor());

        // Iterating every set of entry in the HashMap
        for (Map.Entry<ExpressioBooleana,Set<KeyP>> set : Index.entrySet()) {
            if (set.getKey().evaluate(d)) {
                Set<KeyP> nou = new HashSet<KeyP>();
                nou.add(info);
                nou.addAll(set.getValue());

                set.setValue(nou);
            }
        }
    }

    /**
     * Esborra el document donat de l'endex.
     * @param d Document que es vol esborrar de la llista.
     */
    public void esborratDocument(Document d) {
        KeyP dades = new KeyP(d.getTitol(),d.getAutor());

        for (Set<KeyP> set : Index.values()) {
            Set<KeyP> res = new HashSet<KeyP>();
            for (KeyP p : set) {
                if (!p.equals(dades)) res.add(p);
            }
            set.removeAll(set);
            set.addAll(res);
        }
    }


}