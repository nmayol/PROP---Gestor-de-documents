package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Aquesta view demana les dades a l'usuari per tal de poder llistar els autors segons el prefix indicat
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaDadesPrefix extends JFrame {
    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> historial;
    private JPanel confirm;
    private JPanel data;

    /**
     * Creadora per defecte
     */
    public PantallaDadesPrefix() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        initButtons();
        init_confirm();
        init_dades();
        init_contentPane();
        initPropietats();

    }

    /**
     * Inicialitzadora del JPanel principal
     *
     *
     */
    private void init_contentPane() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(confirm, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        contentPane.add(data, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora del JPanel de confirmacions
     *
     *
     */
    private void init_confirm() {
        confirm = new JPanel();
        confirm.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        confirm.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirm.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        confirm.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora del JPanel de dades
     *
     *
     */
    private void init_dades() {
        final JLabel label1 = new JLabel();
        label1.setText("PREFIX");

        historial = new JComboBox<String>();

        data = new JPanel();
        data.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        data.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        data.add(historial, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora de les propietats de la pantalla
     *
     *
     */
    private void initPropietats() {
        setTitle("DADES PER LLISTAR DOCUMENTS PER PREFIX");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int amplada = (int) Math.round(screenSize.width * 0.30);
        int alcada = (int) Math.round(screenSize.height * 0.20);
        this.setSize(amplada, alcada);
        setLocationRelativeTo(null);

        String[] autor = new String[0];
        try {
            autor = ctrlPresentacio.getPrefixosHistorial();
        } catch (ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        historial.setModel(new DefaultComboBoxModel<String>(autor));
        historial.setEditable(true);
        historial.setSelectedItem("");
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
                    onOK((String) historial.getSelectedItem());
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
     * Funcio per defecte del button buttonOk
     * S'encarrega de continuar amb el flux de l'accio (llistar autors per prefix) passant-ne com a parametre
     * el parametre d'entrada.
     * @param s prefix
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionPrefixNoExisteix
     * @throws ExceptionAutorNoExisteix
     */
    public void onOK(String s) throws ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO, ExceptionPrefixNoExisteix, ExceptionAutorNoExisteix {
        ctrlPresentacio.llistarAutorsPerPrefix(s);
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
