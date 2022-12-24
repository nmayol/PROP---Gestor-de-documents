package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Pantalla per poder llistar i eliminar els usuaris existents
 */
public class PantallaEliminarUsuaris extends JFrame {
    private final CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel llistatUsuarisLabel;
    private JList<String> usuaris_list1;
    private JButton tornarEnrereButton;
    private JLabel missatgeInformatiu;

    /**
     * Creadora de la pantalla
     */
    public PantallaEliminarUsuaris() {
        super();

        ctrlPresentacio = CtrlPresentacio.getInstance();

        this.setSize(1200, 800);
        this.setTitle("Eliminar usuaris");
        setLocationRelativeTo(null);

        llistatUsuarisLabel.setFont(new Font(null, Font.PLAIN, 17));

        missatgeInformatiu.setFont(new Font(null, Font.PLAIN, 13));

        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        llistar_tots_usuaris();

        tornarEnrereButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    tornarEnrere();
                } catch (ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }

            }
        });

        usuaris_list1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    eliminar_usuari(usuaris_list1.getSelectedValue());
                } catch (ExceptionUsuariNoExisteix | ExceptionDirectoriNoEliminat | ExceptionContrasenyaIncorrecta |
                         ExceptionDirNoTrobat | ExceptionIO | ExceptionCampsBuits ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Funcio per defecte del boto cancel
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * Llista tots els usuaris existents a l'aplicacio en el moment en el que es crida la funcio
     */
    public void llistar_tots_usuaris() {
        String[] tots_usuaris = ctrlPresentacio.llistar_tots_usuaris();
        DefaultListModel<String> us = new DefaultListModel<>();
        us.addAll(List.of(tots_usuaris));
        usuaris_list1.setModel(us);
        usuaris_list1.setFont(new Font(null, Font.PLAIN, 15));
        usuaris_list1.setVisible(true);
    }

    /**
     * Elimina un usuari de l'aplicacio
     * @param usuari Nom de l'usuari que volem eliminar
     * @throws ExceptionUsuariNoExisteix
     * @throws ExceptionDirectoriNoEliminat
     * @throws ExceptionContrasenyaIncorrecta
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionCampsBuits
     */
    private void eliminar_usuari(String usuari) throws ExceptionUsuariNoExisteix, ExceptionDirectoriNoEliminat, ExceptionContrasenyaIncorrecta, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if (usuari != null) {
            ctrlPresentacio.guardar_nom_usuari(usuari);
            JFrame jFrame = new JFrame();
            //String getMessage = JOptionPane.showInputDialog(jFrame, "Introdueix la contrasenya");
            if (ctrlPresentacio.demana_dades("Vols eliminar l'usuari "+ usuari + "?")) {
                String getMessage = JOptionPane.showInputDialog(jFrame, "Introdueix la contrasenya");
                if (String.valueOf(getMessage).equals("")) throw new ExceptionCampsBuits();
                else {
                    ctrlPresentacio.guardar_nom_contrasenya(String.valueOf(getMessage));
                    ctrlPresentacio.eliminar_usuari();
                    ctrlPresentacio.actualitzar_usuaris_PantallaEliminarUsuari();
                    JOptionPane.showMessageDialog(jFrame, "S'ha pogut eliminar l'usuari correctament");
                }
            }
        }
    }

    /**
     * Funció per defecte del boto Tornar Enrere.
     * Retorna a la Pantalla Iniciar Sessio
     */
    private void tornarEnrere() throws ExceptionNoSessioActiva {
        this.dispose();
        ctrlPresentacio.controlaVista("PantallaIniciarSessio");
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        usuaris_list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        usuaris_list1.setModel(defaultListModel1);
        panel1.add(usuaris_list1, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        tornarEnrereButton = new JButton();
        tornarEnrereButton.setText("Tornar Enrere");
        panel1.add(tornarEnrereButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        llistatUsuarisLabel = new JLabel();
        llistatUsuarisLabel.setText("Llistat usuaris");
        panel1.add(llistatUsuarisLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        missatgeInformatiu = new JLabel();
        missatgeInformatiu.setText("Selecciona l'usuari que vulguis eliminar:");
        panel1.add(missatgeInformatiu, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
