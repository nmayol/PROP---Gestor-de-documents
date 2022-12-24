package utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Paula Grau
 * És una parella de dos paràmetres
 * @param <F> Primer paràmetre de la parella
 * @param <S> Segon paràmetre de la parella
 */
public class PairP<F, S> implements Serializable {

    /**
     * Primer paràmetre de la parella
     */
    private final F first;
    /**
     * Segon paràmetre de la parella
     */
    private final S second;

    /**
     * Creadora
     * @param first Nom del primer paràmetre de la parella
     * @param second Nom del segon paràmetre de la parella
     */
    public PairP(F first, S second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Getter del primer paràmetre de la parella
     * @return Retorna el primer paràmetre de la parella
     */
    public F getFirst() {
        return first;
    }

    /**
     * Getter del segon paràmetre de la parella
     * @return Retorna el segon paràmetre de la parella
     */
    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairP<?, ?> pairP = (PairP<?, ?>) o;
        return Objects.equals(first, pairP.first) && Objects.equals(second, pairP.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
