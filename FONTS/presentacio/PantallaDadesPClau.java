package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Aquesta view demana les dades a l'usuari per tal de poder llistar document/s segons les paraules claus o query indicat
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaDadesPClau extends JFrame {
    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner K;
    private JComboBox<String> historial;
    private JPanel confirm;
    private JPanel data;

    /**
     * Creadora per defecte
     */
    public PantallaDadesPClau() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        init_dades();
        init_confirm();
        init_contentPane();
        initPropietats();
        initButtons();

        historial.setSelectedItem("");
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
     * Inicialitzadora del JPanel de dades
     */
    private void init_dades() {
        data = new JPanel();
        data.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("PARAULES CLAU");
        data.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        K = new JSpinner();
        data.add(K, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("NUMERO");
        data.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        historial = new JComboBox<String>();
        data.add(historial, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }

    /**
     * Inicialitzadora de les propietats de la pantalla
     */
    private void initPropietats() {
        setTitle("DADES PER LLISTAR DOCUMENTS PER PARAULES CLAU");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(500, 200);
        setLocationRelativeTo(null);

        String[] autor = new String[0];
        try {
            autor = ctrlPresentacio.getParaulesHistorial();
        } catch (ExceptionNoSessioActiva e) {
            throw new RuntimeException(e);
        }
        historial.setModel(new DefaultComboBoxModel<String>(autor));
        historial.setEditable(true);
    }

    /**
     * Inicialitzadora dels buttons, les seves propietats i els seus listeners
     */
    private void initButtons() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionNoEnterValid | ExceptionDocumentNoExisteix | ExceptionNoSessioActiva |
                         ExceptionFormatNoValid | ExceptionDocumentJaExisteix | ExceptionConsultaLimitParametres |
                         ExceptionNotPrimaryKeys | ExceptionAutorNoExisteix | ExceptionAutorJaExisteix |
                         ExceptionDirNoTrobat | ExceptionIO | ExceptionCampsBuits ex) {
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
     * Funcio per defecte del button buttonOk
     * S'encarrega de continuar amb el flux de l'accio (llistar documents query) passant-ne com a parametre
     * el valor escrit en el JComboBox historial i l'enter inidicat en JSpinner K.
     * @throws ExceptionNoEnterValid
     * @throws ExceptionDocumentNoExisteix
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionFormatNoValid
     * @throws ExceptionDocumentJaExisteix
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionNotPrimaryKeys
     * @throws ExceptionAutorNoExisteix
     * @throws ExceptionAutorJaExisteix
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionCampsBuits
     */
    public void onOK() throws ExceptionNoEnterValid, ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionConsultaLimitParametres, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        ctrlPresentacio.llistarDocumentsPClau((String) historial.getSelectedItem(), (int) K.getValue());
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