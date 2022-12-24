package test.persistencia;

import org.junit.Before;
import org.junit.Test;
import persistencia.ConsultaPersistencia;

public class TestConsultaPersistencia {

    ConsultaPersistencia consultaPersistencia;

    @Before
    public void before(){
        consultaPersistencia = new ConsultaPersistencia("EXE/test/persistencia/usuaris");
    }

    /** Només es fa l'operació escriureObjecte de IOFitxers, ja testejada.
     * @see TestIOFitxers#testEscriureObjecte()
     */
    @Test
    public void testGuardarConsulta(){

    }

    @Test
    public void testGetConsultes(){

    }
}
