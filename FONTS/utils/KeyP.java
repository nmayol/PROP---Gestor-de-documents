package utils;

import java.io.Serializable;

/**
 * @author Joan Vazquez
 * És una parella de dos parametres
 */
public class KeyP implements Serializable {

    /**
     * Primer parametre de la parella
     */
    private final String k1;

    /**
     * Segon parametre de la parella
     */
    private final String k2;

    /**
     * Creadora
     * @param k1 Nom del primer parametre de la parella
     * @param k2 Nom del segon parametre de la parella
     */
    public KeyP(String k1, String k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    /**
     * Getter del primer parametre de la parella
     * @return Retorna el primer parametre de la parella
     */
    public String getK1() {
        return k1;
    }

    /**
     * Getter del segon parametre de la parella
     * @return Retorna el segon parametre de la parella
     */
    public String getK2() {
        return k2;
    }

    /**
     * Comparador de instancies
     * @return Retorna l'ordre de la comparacio
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyP)) return false;
        KeyP key = (KeyP) o;
        return k1.equals(key.k1) && k2.equals(key.k2);
    }

    /**
     * Valor hash de la clau
     * @return Retorna el valor hash
     */
    @Override
    public int hashCode() {
        return k1.hashCode()+k2.hashCode();
    }

}
