package test.persistencia;

import org.junit.Before;
import org.junit.Test;
import persistencia.IndexPersistencia;

import static org.junit.Assert.assertEquals;

public class TestIndexPersistencia {

    IndexPersistencia indexPersistencia;
    @Before
    public void before() {
        indexPersistencia = new IndexPersistencia("EXE/test/persistencia/usuaris");
    }

    /** Només es fa l'operació escriureObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testEscriureObjecte()
     */
    @Test
    public void testGuardarIA() {

    }

    /** Només es fa l'operació escriureObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testEscriureObjecte()
     */
    @Test
    public void testGuardarIDA() {
    }

    /** Només es fa l'operació escriureObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testEscriureObjecte()
     */
    @Test
    public void testGuardarIEB() {
    }

    /** Només es fa l'operació llegirObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testLlegirObjecte()
     */
    @Test
    public void testGetIA() {
    }

    /** Només es fa l'operació llegirObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testLlegirObjecte()
     */
    @Test
    public void testGetIDA() {
    }

    /** Només es fa l'operació llegirObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testLlegirObjecte()
     */
    @Test
    public void testGetIEB() {
    }
}
