package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Aquesta view demana les noves dades del document a l'usuari.
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaNovesDades extends JDialog {
    private final CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField AUTOR_NOU;
    private JTextField TITOL_NOU;
    private JTextArea CONTINGUT_NOU;
    private JPanel confirm;
    private JPanel data;

    /**
     * Creadora per defecte
     */
    public PantallaNovesDades() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();
        setTitle("PANTALLA DADES MODIFICAR DOCUMENT");

        init_dades();
        init_confirm();
        init_contentPane();

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(500, 500);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionDocumentNoExisteix | ExceptionFormatNoValid | ExceptionNoSessioActiva |
                         ExceptionDocumentJaExisteix | ExceptionNotPrimaryKeys | ExceptionAutorNoExisteix |
                         ExceptionAutorJaExisteix | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | ExceptionIO |
                         ExceptionCampsBuits ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                } catch (IOException ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
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
     *
     *
     */
    private void init_dades() {
        data = new JPanel();
        data.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        AUTOR_NOU = new JTextField();
        data.add(AUTOR_NOU, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        TITOL_NOU = new JTextField();
        data.add(TITOL_NOU, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("AUTOR:");
        data.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("TITOL:");
        data.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("CONTINGUT:");
        data.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CONTINGUT_NOU = new JTextArea();
        data.add(CONTINGUT_NOU, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     *  Funcio per defecte del button buttonOk
     *  S'encarrega de continuar amb el flux de l'accio (modificar document) passant-ne les noves dades d'aquest.
     * @throws ExceptionDocumentNoExisteix
     * @throws ExceptionFormatNoValid
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionDocumentJaExisteix
     * @throws ExceptionNotPrimaryKeys
     * @throws ExceptionAutorNoExisteix
     * @throws ExceptionAutorJaExisteix
     * @throws ExceptionDirectoriNoEliminat
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws IOException
     * @throws ExceptionCampsBuits
     */
    public void onOK() throws ExceptionDocumentNoExisteix, ExceptionFormatNoValid, ExceptionNoSessioActiva, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, IOException, ExceptionCampsBuits {
        ctrlPresentacio.getDadesDocumentNou(AUTOR_NOU.getText(), TITOL_NOU.getText(), CONTINGUT_NOU.getText());
        ctrlPresentacio.popup("s'ha modificat el document", "Informacio");
        dispose();
    }


    /**
     * Funcio per defecte del button buttonCancel
     * Tanca la pantalla
     *
     *
     */
    public void onCancel() {
        dispose();
    }

}