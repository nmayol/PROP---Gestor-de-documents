package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/**
 * Aquesta view demana les dades a l'usuari per tal de poder llistar els documents segons l'autor indicat
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaDadesAutors extends JFrame {
    private final CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> autors;
    private JPanel confirm;
    private JPanel data;

    /**
     *  Creadora per defecte
     * @throws ExceptionNoSessioActiva
     */
    public PantallaDadesAutors() throws ExceptionNoSessioActiva {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        initButtons();
        init_dades();
        init_confirm();
        init_contentPane();
        initPropietats();

        autors.setSelectedItem("");

    }

    /**
     * Creadora amb parametres
     * @param DEF_Autor es el nom de l'autor que esta seleccionat en la taula de la pantalla principal i per millorar la
     *                 usabilitat de la aplicacio, es posa per defecte.
     * @throws ExceptionNoSessioActiva
     */
    public PantallaDadesAutors(String DEF_Autor) throws ExceptionNoSessioActiva {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        initButtons();
        init_dades();
        init_confirm();
        init_contentPane();
        initPropietats();


        autors.setSelectedItem(DEF_Autor);
    }

    /**
     * Inicialitzadora dels buttons, les seves propietats i els seus listeners
     */
    private void initButtons() {

        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionNoSessioActiva | ExceptionConsultaLimitParametres | ExceptionAutorNoExisteix |
                         ExceptionDirNoTrobat | ExceptionIO | ExceptionPrefixNoExisteix ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               actionPerformed_TornarEnrere(event);
            }
        });
    }

    /**
     * Inicialitzadora de les propietats de la pantalla
     * @throws ExceptionNoSessioActiva
     */
    private void initPropietats() throws ExceptionNoSessioActiva {
        setTitle("PANTALLA DADES DOCUMENTS AUTORS");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(500, 150);
        setLocationRelativeTo(null);

        String[] autor = ctrlPresentacio.getAutorsHistorial();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int i = 0; i < autor.length; ++i) model.addElement(autor[i]);
        autors.setModel(model);
        autors.setEditable(true);
    }

    /**
     * Inicialitzadora del JPanel principal
     */
    private void init_contentPane() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(confirm, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        contentPane.add(data, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora del JPanel de confirmacions
     */
    private void init_confirm() {
        final Spacer spacer1 = new Spacer();

        confirm = new JPanel();
        confirm.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        confirm.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirm.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirm.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

    }

    /**
     * Inicialitzadora del JPanel de dades
     */
    private void init_dades() {
        final JLabel label1 = new JLabel();
        label1.setText("AUTORS");

        autors = new JComboBox<String>();

        data = new JPanel();
        data.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        data.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        data.add(autors, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }

    /**
     * Funcio per defecte del button buttonOk
     * S'encarrega de continuar amb el flux de l'accio (llistar documents segons autor) passant-ne com a paramtre
     * el nom escrit en el JComboBox d'autor.
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionAutorNoExisteix
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionPrefixNoExisteix
     */
    public void onOK() throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix {
        ctrlPresentacio.llistarDocumentsAutor((String) autors.getSelectedItem());
        this.dispose();
    }

    /**
     * Funcio per defecte del button buttonCancel
     * Tanca la pantalla
     */
    public void actionPerformed_TornarEnrere(ActionEvent event) {
        this.dispose();
    }

}