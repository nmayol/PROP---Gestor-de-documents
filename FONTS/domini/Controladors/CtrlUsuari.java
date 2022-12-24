package domini.Controladors;

import Exceptions.*;
import domini.*;
import java.util.ArrayList;


public class CtrlUsuari{
    private ArrayList<Usuari> usuaris;
    private int usuariActiu;

    /** Constructora.
     *
     */
    public CtrlUsuari(){
        usuaris = new ArrayList<>(0);
        usuariActiu = -1;
    }

    /** Retorna el numero de usuaris registrats
     */
    public int numeroUsuaris(){
        return usuaris.size();
    }

    /** Afegeix un usuari a l'aplicacio
     * @param usuari Nou usuari
     * @throws ExceptionUsuariJaExisteix Ja existeix l'usuari (mateix nom)
     * @see Usuari
     */
    public void afegirUsuari(Usuari usuari) throws ExceptionUsuariJaExisteix {
        if(indexUsuari(usuari.getNom()) != -1) {
            throw new ExceptionUsuariJaExisteix(usuari.getNom());
        }
        else {
            usuaris.add(usuari);
        }
    }

    /** Elimina un usuari de l'aplicacio
     * @param nom Nom de l'usuari a eliminar
     * @param contrasenya Contrasenya de l'usuari a eliminar
     * @throws ExceptionUsuariNoExisteix No existeix l'usuari
     * @throws ExceptionContrasenyaIncorrecta Contrasenya incorrecta
     */
    public void eliminarUsuari(String nom, String contrasenya) throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta {
        int index = indexUsuari(nom);
        if(index == -1) {
            throw new ExceptionUsuariNoExisteix(nom);
        }else if(!usuaris.get(index).getContrasenya().equals(contrasenya)) {
            throw new ExceptionContrasenyaIncorrecta(nom);
        }else {
            usuaris.remove(indexUsuari(nom));
            // Usuari actiu es el que vull eliminar
            if(usuariActiu != -1 && usuaris.get(usuariActiu).getNom().equals(nom)) usuariActiu = -1;
        }
    }

    /** Es comença una sessio per un usuari, l'usuari actiu passa a ser aquest.
     * @param nom Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @throws ExceptionSessioJaActiva Ja hi ha un usuari actiu
     * @throws ExceptionUsuariNoExisteix No existeix l'usuari pel qual vull iniciar sessio
     * @throws ExceptionContrasenyaIncorrecta Contrasenya incorrecta
     */
    public void iniciarSessio(String nom, String contrasenya)
            throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta, ExceptionSessioJaActiva {
        if(usuariActiu != -1){
            throw new ExceptionSessioJaActiva();
        }
        int index = indexUsuari(nom);
        if(index == -1) {
            throw new ExceptionUsuariNoExisteix(nom);
        }
        else if(!usuaris.get(index).getContrasenya().equals(contrasenya)) {
            throw new ExceptionContrasenyaIncorrecta(nom);
        }
        usuariActiu = index;
    }

    /** Tanca la sessio actual
     * @throws ExceptionNoSessioActiva No hi ha un usuari actiu
     */
    public void tancarSessio() throws ExceptionNoSessioActiva {
        if (usuariActiu == -1) throw new ExceptionNoSessioActiva();
        usuariActiu = -1;
    }

    /** Retorna el conjunt de tots els usuaris registrats a l'aplicacio
     */
    public ArrayList<Usuari> getUsuaris(){
        return usuaris;
    }

    /** Estableix el conjunt d'usuaris.
     *
     * @param usuaris Conjunt d'usuaris
     */
    public void setUsuaris(ArrayList<Usuari> usuaris){
        this.usuaris = usuaris;
    }

    /** Retorna l'usuari en sessio o null.
     */
    public Usuari getUsuariActiu() {
        if(usuariActiu == -1) return null;
        return usuaris.get(usuariActiu);
    }

    private int indexUsuari(String nom){
        for(int i = 0; i < usuaris.size(); i++){
            if(usuaris.get(i).getNom().equals(nom)) return i;
        }
        return -1;
    }
}
