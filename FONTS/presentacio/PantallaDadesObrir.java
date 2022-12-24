package presentacio;

import Exceptions.ExceptionCampsBuits;
import Exceptions.ExceptionDocumentNoExisteix;
import Exceptions.ExceptionNoSessioActiva;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Aquesta view demana les dades a l'usuari per tal de poder obrir un document segons les claus indicades
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaDadesObrir extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> autors;
    private JComboBox<String> titols;
    private JPanel confirm;
    private JPanel data;

    CtrlPresentacio ctrlPresentacio;

    /**
     * Creadora per defecte
     */
    public PantallaDadesObrir() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        init_dades();
        init_confirm();
        init_contentPane();

        try {
            initPropietats();
        } catch (ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        initButtons();

        titols.setSelectedItem("- titol -");
        autors.setSelectedItem("- autor -");
    }


    /**
     * Creadora amb parametres
     *
     * @param DEF_Autor es l'autor del document que esta seleccionat en la taula de la pantalla principal i per millorar la
     *                  usabilitat de la aplicacio, es posa per defecte.
     * @param DEF_Autor es el titol del document que esta seleccionat en la taula de la pantalla principal i per millorar la
     *                  usabilitat de la aplicacio, es posa per defecte.
     */
    public PantallaDadesObrir(String DEF_Autor, String DEF_Titol) {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        init_dades();
        init_confirm();
        init_contentPane();
        try {
            initPropietats();
        } catch (ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        initButtons();

        if (DEF_Titol != null) titols.setSelectedItem(DEF_Titol);
        else titols.setSelectedItem("");
        if (DEF_Autor != null) autors.setSelectedItem(DEF_Autor);
        else autors.setSelectedItem("");
    }


    /**
     * Inicialitzadora del JPanel principal
     */
    private void init_contentPane() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(data, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        contentPane.add(confirm, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
    }


    /**
     * Inicialitzadora del JPanel de dades
     */
    private void init_dades() {
        data = new JPanel();
        data.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("AUTOR:");
        data.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("TITOL:");
        data.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autors = new JComboBox<String>();
        data.add(autors, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titols = new JComboBox<String>();
        data.add(titols, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }


    /**
     * Inicialitzadora del JPanel de confirmacions
     */
    private void init_confirm() {
        confirm = new JPanel();
        confirm.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        confirm.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        confirm.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        confirm.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora de les propietats de la pantalla
     */
    private void initPropietats() throws ExceptionNoSessioActiva {
        setTitle("PANTALLA DADES OBRIR DOCUMENT");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(500, 225);
        setLocationRelativeTo(null);

        DefaultComboBoxModel<String> a = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> t = new DefaultComboBoxModel<>();

        ArrayList<String[]> data = null;
        try {
            data = ctrlPresentacio.getSemblantsHistorial();
        } catch (ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        for (int i = 0; i < data.size(); ++i){
            a.addElement(data.get(i)[0]);
            t.addElement(data.get(i)[1]);
        }

        autors.setModel(a);
        titols.setModel(t);

        autors.setEditable(true);
        titols.setEditable(true);
    }


    /**
     * Inicialitzadora dels buttons, les seves propietats i els seus listeners
     */
    private void initButtons() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionCampsBuits | ExceptionDocumentNoExisteix | ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_TornarEnrere(event);
            }
        });
    }

    /**
     *  Funcio per defecte del button buttonOk
     *  S'encarrega de continuar amb el flux de l'accio (obrir document) passant-ne com a parametre
     *  el valor escrit en el JComboBox d'autor i el valor escrit en el JComboBox del titol.
     * @throws ExceptionCampsBuits
     * @throws ExceptionDocumentNoExisteix
     * @throws ExceptionNoSessioActiva
     */
    public void onOK() throws ExceptionCampsBuits, ExceptionDocumentNoExisteix, ExceptionNoSessioActiva {
        String a = (String) autors.getSelectedItem();
        String t = (String) titols.getSelectedItem();
        if (!a.equals("") && !t.equals("")) {
            if (ctrlPresentacio.existeix_document(a, t)) {
                dispose();
                ctrlPresentacio.ObreDocument(a, t);
            }
            else throw new ExceptionDocumentNoExisteix(a, t);
        } else throw new ExceptionCampsBuits();
    }

    /**
     * Funcio per defecte del button buttonCancel
     * Tanca la pantalla
     */
    public void actionPerformed_TornarEnrere(ActionEvent event) {
        this.dispose();
    }

}