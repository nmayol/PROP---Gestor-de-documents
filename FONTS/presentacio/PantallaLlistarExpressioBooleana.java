package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Classe corresponent a la pantalla que fa cerques amb una expressió booleana.
 *
 * @author Neus Mayol
 */
public class PantallaLlistarExpressioBooleana extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JList<String> Expr;
    private final DefaultListModel<String> Model;
    private DefaultTableModel ModelTaula;
    private JTable Docs;
    private JButton obreButton;
    private JButton afegeixExpressioButton;
    private JButton esborraButton;


    private CtrlPresentacio ctrlPresentacio;

    /**
     * Creadora
     */
    public PantallaLlistarExpressioBooleana() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        setContentPane(contentPane);
        setModal(true);
        double screenSizeH = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
        double screenSizeW = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5;
        setSize((int) screenSizeW, (int) screenSizeH);

        obreButton.setEnabled(false);

        Expr.setLayout(new FlowLayout());
        Expr.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Docs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setLocationRelativeTo(null);
        Model = new DefaultListModel<>();
        Expr.setModel(Model);
        setResizable(false);

        ctrlPresentacio.popup("A vegades l'index simplifica automaticament algunes expressions", "Info");

        ModelTaula = new DefaultTableModel(null, new String[]{"Titol", "Autor"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        try {
            inicialitzarExpressions();
            inicialitzarLlistaDocs();
        } catch (ExceptionExpressioBooleanaIncorrecta | ExceptionNoSessioActiva | ExceptionConsultaLimitParametres |
                 ExceptionDirNoTrobat | ExceptionIO e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }


        //ASSIGNAR MODEL
        Docs.setModel(ModelTaula);
        Expr.setModel(Model);

        Expr.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    inicialitzarLlistaDocs();
                } catch (ExceptionExpressioBooleanaIncorrecta | ExceptionIO | ExceptionNoSessioActiva |
                         ExceptionConsultaLimitParametres | ExceptionDirNoTrobat ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        ListSelectionModel selectionModel = Docs.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                obreButton.setEnabled(selectionModel.getSelectedIndices().length == 1);
            }
        });
        afegeixExpressioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String getMessage = JOptionPane.showInputDialog(new JFrame(), "Introdueix l'expressió booleana.");
                try {
                    ctrlPresentacio.novaEB(getMessage);
                    inicialitzarExpressions();
                } catch (ExceptionExpressioBooleanaIncorrecta | ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        esborraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ctrlPresentacio.netejarIndexEB();
                } catch (ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
                try {
                    inicialitzarExpressions();
                } catch (ExceptionExpressioBooleanaIncorrecta | ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        obreButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                onObre();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Obre el document seleccionat
     */
    public void onObre() {
        String s = (String) Docs.getValueAt(Docs.getSelectedRow(), 1), t = (String) Docs.getValueAt(Docs.getSelectedRow(), 0);
        Expr.clearSelection();
        ctrlPresentacio.ObreDocument(s, t);
    }

    /**
     * Llista totes les expressions de l'index
     * @throws ExceptionExpressioBooleanaIncorrecta
     * @throws ExceptionNoSessioActiva
     */
    private void inicialitzarExpressions() throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva {
        ArrayList<String> Expressions = ctrlPresentacio.getIndexExpressions();
        Model.clear();
        for (int i = 0; i < Expressions.size(); ++i) {
            Model.addElement(Expressions.get(i));
        }
    }


    /**
     * Llista tots els documents que compleixen l'expressio seleccionada
     * @throws ExceptionExpressioBooleanaIncorrecta
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     */
    private void inicialitzarLlistaDocs() throws ExceptionExpressioBooleanaIncorrecta, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO {
        ModelTaula.setRowCount(0);
        if (Expr.getSelectedIndex() >= 0) {
            String expr = Expr.getSelectedValue();
            ArrayList<String[]> res = ctrlPresentacio.getDocumentsExpressio(expr);
            for (int i = 0; i < res.size(); ++i) {
                ModelTaula.addRow(new String[]{res.get(i)[0], res.get(i)[1]});
            }
        }
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
     * inspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Expr = new JList();
        scrollPane1.setViewportView(Expr);
        final JLabel label1 = new JLabel();
        label1.setText("Expressió Booleana");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        afegeixExpressioButton = new JButton();
        afegeixExpressioButton.setText("Afegeix Expressió");
        panel3.add(afegeixExpressioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        esborraButton = new JButton();
        esborraButton.setText("Esborra Historial");
        panel3.add(esborraButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Docs = new JTable();
        scrollPane2.setViewportView(Docs);
        final JLabel label2 = new JLabel();
        label2.setText("Documents Resultants");
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        obreButton = new JButton();
        obreButton.setText("Obre");
        panel5.add(obreButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel6.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Enrere");
        panel7.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * inspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
