package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.compare;
import static java.lang.Math.min;

/**
 * Correspon a l'estructura de dades d'un arbre binari.
 * @author Neus Mayol
 */
public class Tree<T> implements Serializable {
    private Node<T> root;

    /**
     * Constructora que assigna un valor a l'arrel del nou arbre.
     * @param rootData Les dades que ha de contenir l'arrel del nou arbre.
     */
    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.l = new Node<T>();
        root.r = new Node<T>();
    }

    /**
     * Constructora per defecte.
     */
    public Tree() {
        root = new Node<T>();
        root.l = new Node<T>();
        root.r = new Node<T>();
    }

    /**
     * Getter de l'arrel de l'arbre.
     */
    public Node<T> getRoot() {
        return root;
    }

    /**
     * Estructura de dades que correspon a un node d'un arbre binari.
     * @author Neus Mayol
     */
    public static class Node<T> implements Serializable{
        private T data;
        private Node<T> l;
        private Node<T> r;

        /**
         * Getter del contingut de l'arrel de l'arbre.
         */
        public T getData() { return data; }

        /**
         * Setter del contingut de l'arrel de l'arbre.
         * @param d dades que ha de contenir el parametre implicit.
         */
        public void setData (T d) { data = d; }

        /**
         * Getter del contingut del node fill esquerre de l'arbre.
         */
        public Node<T> getL() { return l; }

        /**
         * Setter del contingut del node fill esquerre de l'arbre.
         * @param left Node que s'assignara al fill esquerre del parametre implicit.
         */
        public void setL (Node<T> left) { l = left; }

        /**
         * Getter del contingut del node fill dret de l'arbre.
         */
        public Node<T> getR() { return r; }

        /**
         * Setter del contingut del node fill dret de l'arbre.
         * @param right Node que s'assignara al fill dret del parametre implicit.
         */
        public void setR (Node<T> right) { r = right; }


    }
}
