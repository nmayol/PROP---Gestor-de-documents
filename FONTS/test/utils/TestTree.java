package test.utils;


import org.junit.Test;
import utils.Tree;

import static junit.framework.TestCase.assertEquals;

public class TestTree {

    @Test
    public void getRoot() {
        Tree t = new Tree();

        // CAS 1: Root es buit
        Tree.Node obj = t.getRoot();
        assertEquals(null,obj.getData());

        // CAS 2: Root es ple
        Object o = new Object();
        t = new Tree(o);
        obj = t.getRoot();
        assertEquals(o,obj.getData());

    }



}
