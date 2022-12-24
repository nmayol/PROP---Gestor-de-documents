package test.utils;

import org.junit.Test;
import utils.Tree;

import static junit.framework.TestCase.assertEquals;

public class TestNode {

    @Test
    public void getData() {
        Tree.Node N = new Tree.Node<>();
        Object o = new Object();
        N.setData(o);

        assertEquals(o,N.getData());
    }


    @Test
    public void setData() {
        Tree.Node N = new Tree.Node<>();
        Object o = new Object();
        N.setData(o);

        assertEquals(o,N.getData());
    }

    @Test
    public void getL() {
        Tree.Node M = new Tree.Node<>();
        Tree.Node N = new Tree.Node<>();
        N.setL(M);
        assertEquals(M,N.getL());
    }

    @Test
    public void getR() {
        Tree.Node M = new Tree.Node<>();
        Tree.Node N = new Tree.Node<>();
        N.setR(M);
        assertEquals(M,N.getR());
    }

    @Test
    public void setL() {
        Tree.Node M = new Tree.Node<>();
        Tree.Node N = new Tree.Node<>();
        N.setL(M);
        assertEquals(M,N.getL());
    }

    @Test
    public void setR() {
        Tree.Node M = new Tree.Node<>();
        Tree.Node N = new Tree.Node<>();
        N.setR(M);
        assertEquals(M,N.getR());
    }


}
