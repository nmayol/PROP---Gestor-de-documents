package domini;

/** Representa un usuari de l'aplicacio.
 * @author tomas.calaf.marti
 */
public class Usuari {
    private String nom;
    private String contrasenya;

    public Usuari(String nom, String contrasenya) {
        this.nom = nom;
        this.contrasenya = contrasenya;
    }

    /** Retorna el nom de l'usuari
     *
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /** Asigna un nom a l'usuari
     *
     * @param nom Nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** Retorna la contrasenya de l'usuari
     *
     * @return String
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /** Assigna una contrasenya a l'usuari
     *
     * @param contrasenya Contrasenya
     */
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
}