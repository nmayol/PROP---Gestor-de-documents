package utils;


import Exceptions.ExceptionExpressioBooleanaIncorrecta;
import domini.Document;
import utils.Tree;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Character.compare;
import static java.lang.Math.*;

/**
 * s una classe que guarda una expressio booleana en forma d'arbre binari per tal de facilitar la seva avaluacio.
 * La seva funcio es ser un element de l'index que emmagatzema les expressions booleanes.
 * @author Neus Mayol
 */
public class ExpressioBooleana implements Serializable {
    Tree<String> expressio;
    private static final ArrayList<Character> prioritatAsc = new ArrayList<Character>();

    /**
     * Creadora, retorna una expressio booleana l'arbre de la qual es buit.
    */
    public ExpressioBooleana() { this.expressio = new Tree<>(); }

    /**
    * Creadora, retorna una expressio booleana amb l'arbre corresponent al parametre d'entrada.
    * @param s expressio booleana introduida per l'usuari
    */
    public ExpressioBooleana(String s) throws ExceptionExpressioBooleanaIncorrecta {
        this.expressio = new Tree<>();
        setExpressio(s);
    }

    /**
     * @return L'arbre de l'expressio
     */
    public Tree<String> getExpressio() { return expressio; }

    /**
     * Afegeix una expressio a l'arbre, fent les comprovacions pertinents.
     * @param expr Expressio introduïda per l'usuari.
     * @throws ExceptionExpressioBooleanaIncorrecta En cas que l'usuari no hagi introduït be l'expressio.
     */
    public void setExpressio(String expr) throws ExceptionExpressioBooleanaIncorrecta {
        String expr2 = tradueixExpressio(expr);
        if (!comprovaExpressio(expr2)) throw new ExceptionExpressioBooleanaIncorrecta(expr);
        ConstrueixArbre(expr2,expressio.getRoot());
    }

    /**
     * Fa les modificacions pertinents per transformar una expressio booleana introduïda per l'usuari
     * a un format unitari, per facilitar despres la construccio de l'arbre.
     * @param expr expressio booleana introduïda per l'usuari
     * @return L'expressio traduïda.
     */
    public String tradueixExpressio(String expr) {
        int countkeys = 0;
        boolean commas = false;
        int i = 0;
        while  (i < expr.length()) {
            char c = expr.charAt(i);
            if (compare(c, '{') == 0 ) ++countkeys;
            else if (compare(c, '}') == 0 ) --countkeys;
            else if (compare(c, '"') == 0) commas = !commas;

            else if (compare(c, ' ') == 0 && !commas) {
                String expr1 = expr.substring(0, i);
                if (countkeys != 0) expr1 += '&';
                expr = expr1 + expr.substring(i + 1);
                if (countkeys == 0) --i;
            }
            ++i;
        }
        expr = expr.replace("{", "(");
        expr = expr.replace("}", ")");

        return expr;
    }

    /**
     * Comprova que l'usuari no s'hagi equivocat en introduir l'expressio.
     * @param expr expressio booleana ja passada per la funcio de traduccio.
     * @return Cert si l'expressio es correcta, Fals altrament
     */
    public boolean comprovaExpressio(String expr) {
        if (expr.isBlank()) return false;
        int cnt = 0;
        boolean correcte = true, commas = false;
        for (int i = 0; correcte && i < expr.length(); ++i) {
            char c = expr.charAt(i);

            if (c == '"') commas = !commas; // comprovar que els parentesis concordin
            else if (!commas && c == '(') ++cnt;
            else if (!commas && c == ')') --cnt;
            else if (!commas && c == '!') // comprovar que un signe negat no estigui al final de l'expressio, que no estigui abans d'un operador
                correcte = correcte && (i != expr.length()-1) && !(expr.charAt(i+1) == '&') && !(expr.charAt(i+1) == '|');
            else if (!commas && (c == '|' || c == '&'))
                correcte = correcte && (i != 0) && (i != expr.length()-1) && !(expr.charAt(i+1) == '&') && !(expr.charAt(i+1) == '|') && !(expr.charAt(i-1) == '&') && !(expr.charAt(i-1) == '|');

            correcte = correcte && (cnt >= 0);
        }
        return (cnt == 0 && correcte && !commas);

    }

    /**
     * Construeix l'arbre de l'expressio booleana.
     * @param expr expressio booleana introduïda per l'usuari ja traduïda per la funcio de traduccio.
     * @param arrel correspon a l'arrel de l'arbre sobre el qual construirem l'expressio.
    */
    public void ConstrueixArbre(String expr, Tree.Node<String> arrel) {
        // Si expr es buida, haurem arribat a un cas base

        if (0 != expr.length()) { // expr no es buida

            /* Recordem que: (mirar funcio BuscarInici)
                - inici = -1 --> l'expressio nomes conte variables
                - inici >= 0 --> l'expressio contindra la connectiva logica per on s'ha de començar el nou arbre.
            */
            int inici = BuscarInici(expr);
            while (inici != -1 && (expr.charAt(inici) == ')' || expr.charAt(inici) == '(')) {
                String aux = expr.substring(0,inici) + expr.substring(inici+1);
                expr = aux;
                inici = BuscarInici(expr);
            }


            if (inici == -1) { // CAS 1: L'expressio nomes son literals (o variables) --> hem arribat a una full
                if (compare(expr.charAt(0),'"') == 0) // CAS 1.1 de paraules entre "", s'han d'afegir al node sense cometes
                    arrel.setData(expr.substring(1, expr.length()-1));
                else // CAS 1.2 Paraules que no estan entre "" --> es poden afegir tal qual
                    arrel.setData(expr);
            }
            else { // CAS 2: L'expressio conte mes connectors (operadors o parentesis)

                // Creem les substrings fruit de les divisions que farem a continuacio:
                // Esquerre:                Dret:
                String expr1 = new String(), expr2 = new String();

                // i els seus respectius arbres
                Tree.Node<String> Left = new Tree.Node<>(); arrel.setL(Left);
                Tree.Node<String> Right = new Tree.Node<>(); arrel.setR(Right);

                char c = expr.charAt(inici);
                arrel.setData(""+c);
                expr1 = expr.substring(0,max(inici,0));
                expr2 = expr.substring(min(inici+1,expr.length()));
                ConstrueixArbre(expr1, arrel.getL());
                ConstrueixArbre(expr2, arrel.getR());
            }

        }

    }

    /**
     * Busca per quina part de l'expressió s'ha de començar l'arbre.
     * @param expr es una cadena de caracters corresponent a l'expressió introduïda ja traduïda, o be una part d'aquesta.
     * @return La posició del caracter per on s'ha de començar l'arbre al parametre d'entrada.
     */
    public int BuscarInici(String expr){

        prioritatAsc.add('(');
        prioritatAsc.add(')');

        prioritatAsc.add('!');
        prioritatAsc.add('&');
        prioritatAsc.add('|');

        boolean found = false;
        int j = prioritatAsc.size()-1, resultat = -1;

        // Bucle executat fins que trobi una connectiva logica seguint l'ordre de prioritat
        while (!found && j >= 0) {
            int parenthesis = 0; boolean commas = false;
            char op = prioritatAsc.get(j);

            // Bucle que revisa linealment tot expr fins a trobar una expressio d'inici valida
            for (int i = expr.length()-1; !found && i >= 0; --i) {
                char c = expr.charAt(i);

                // Operacions per saber si ens trobem dins un grup de parentesis
                if (compare(c, ')') == 0) ++parenthesis;
                else if (compare(c, '(') == 0) --parenthesis;
                if (compare(c, '"') == 0) commas = !commas;

                // Podem trobar una connectiva si no es troba dins un parentesi o si que s'hi troba pero l'ordre de prioritat j ens hi permet accedir.
                // Si hi accedim perque el caracter c (connectiva), es un parentesi.
                if ((parenthesis == 0 || j <= 1) && !commas && (compare(c, prioritatAsc.get(j)) == 0)) {
                    found = true;
                    resultat = i;
                }
            }
            --j;
        }
        return resultat; // Resultat sera -1 si no trobem cap connectiva. En aquest cas voldra dir que el que ens queden son variables.
    }

    /**
     * Funció recursiva per evaluar documents segons l'expressió booleana.
     * @param cont El contingut del document sobre el qual hem d'avaluar l'expressió.
     * @param T correspon a l'arrel de l'arbre sobre el qual avaluarem l'expressió.
     * @return Cert si el contingut passat compleix l'expressió, fals altrament.
     */
    public Boolean evaluateRec(ArrayList<String> cont, Tree.Node<String> T) {
        if (T != null && T.getData() != null) { // Comprovem si el node es NULL
            if (compare(T.getData().charAt(0), '&') == 0) {         // Cas AND
                Boolean a = a = evaluateRec(cont,T.getL());             // Avaluem l'expressio a l'esquerra
                if (a) return evaluateRec(cont,T.getR());           // Si es certa avaluem l'expressio a la dreta
                else return false;                                  // Si la primera no es certa ja podem retornar fals

            } else if (compare(T.getData().charAt(0), '|') == 0) {  // Cas OR
                Boolean a = evaluateRec(cont,T.getL());             // Avaluem l'expressio a l'esquerra
                if (!a) return evaluateRec(cont,T.getR());          // Si no es certa avaluem l'expressio a la dreta
                else return true;                                   // Si la primera es certa ja podem retornar true

            } else if (compare(T.getData().charAt(0), '!') == 0) {
                return !(evaluateRec(cont,T.getR()));               // Cas !: Neguem el que ens hagi retornat l'expressio filla

            } else if (T.getData().contains(" ")) {
                String[] tall = T.getData().split(" ");

                int i = 0, j = 0;
                while (i < cont.size() && j < tall.length)   {
                    if (cont.get(i).equals(tall[j]))  {
                        i++;  j++;
                        if (j == tall.length) return true;
                    }
                    else { i = i - j + 1;  j = 0; }
                }
                return false;
            } else
                return cont.contains(T.getData());                  // Cas LITERAL: Avaluem si el document conte el literal
        }
        else return true;
    }

    /**
     * Funció per cridar a la funció recursiva per evaluar documents segons l'expressió booleana.
     * @param d El document sobre el qual hem d'avaluar l'expressió.
     * @return Cert si el document passat compleix l'expressió, fals altrament.
     */
    public Boolean evaluate(Document d)  {
        ArrayList<String> cont = d.getContingut();
        if (cont == null) return false;
        return evaluateRec(cont, expressio.getRoot());
    }


    /**
     * Funció que retorna el contingut d'un arbre en preordre.
     * @param arrel Arrel de l'arbre, que correspon a l'arbre que es atribut de la classe.
     * @return String corresponent a l'arbre en preordre.
     */
    public String getArbre(Tree.Node<String> arrel) {
        String res1 = "", res2 = "";
        if (arrel.getData() != null) {
            if (arrel.getL() != null) res1 += getArbre(arrel.getL());
            if (arrel.getR() != null) res2 += getArbre(arrel.getR());
            return res1 + arrel.getData() + res2;
        }
        else return "";

    }

    public String retornaArbre() {
        return getArbre(expressio.getRoot());
    }

}
