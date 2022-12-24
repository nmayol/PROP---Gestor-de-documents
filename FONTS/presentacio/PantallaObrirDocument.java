package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class PantallaObrirDocument extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTextPane textPane1;
    private JLabel Titol;
    private JLabel Autor;
    private JButton modificarButton;
    private JButton guardaCanvisButton;
    private JButton recuperaButton;
    private CtrlPresentacio ctrlPresentacio;

    private String Contingut;
    private Boolean modificar = false;

    public PantallaObrirDocument(String autor, String titol) {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();
        setContentPane(contentPane);
        setModal(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);

        setLocationRelativeTo(null);
        textPane1.setEditable(false);
        Titol.setText(titol);
        Autor.setText(autor);
        try {
            Contingut = String.join(" ", ctrlPresentacio.getContingutDocument(autor, titol));
            textPane1.setText(Contingut);
        } catch (ExceptionDocumentNoExisteix | ExceptionCampsBuits | ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onModificar();
                } catch (ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        guardaCanvisButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    onGuardaCanvis();
                } catch (ExceptionCampsBuits | ExceptionDocumentNoExisteix | ExceptionDocumentJaExisteix | ExceptionNotPrimaryKeys |
                         ExceptionAutorNoExisteix | ExceptionAutorJaExisteix | ExceptionFormatNoValid |
                         ExceptionNoSessioActiva | ExceptionDirNoTrobat | ExceptionDirectoriNoEliminat |
                         ExceptionConsultaLimitParametres | ExceptionIO ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });


        recuperaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRecupera();
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
     * Guarda els canvis que s'han fet despres de la modificacio d'un document
     * @throws ExceptionDocumentNoExisteix No existeix el document.
     * @throws ExceptionFormatNoValid El format introduit no es valid
     * @throws ExceptionNoSessioActiva No hi ha cap sessio activa
     * @throws ExceptionDocumentJaExisteix Ja existeix el document.
     * @throws ExceptionNotPrimaryKeys El document no te claus primaries
     * @throws ExceptionAutorNoExisteix No existeix l'autor.
     * @throws ExceptionAutorJaExisteix Ja existeix l'autor.
     * @throws ExceptionDirectoriNoEliminat
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionCampsBuits
     */
    private void onGuardaCanvis() throws ExceptionDocumentNoExisteix, ExceptionFormatNoValid, ExceptionNoSessioActiva, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionAutorNoExisteix, ExceptionAutorJaExisteix, ExceptionDirectoriNoEliminat, ExceptionConsultaLimitParametres, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if (ctrlPresentacio.accepta_popup("vols modificar el document: " + Titol.getText() + " de l'autor " + Autor.getText() + "?")) {
            ctrlPresentacio.getDadesDocumentAntic(Autor.getText(), Titol.getText());
            ctrlPresentacio.getDadesDocumentNou(Autor.getText(), Titol.getText(), textPane1.getText());
            Contingut = textPane1.getText();
            ctrlPresentacio.popup("S'ha modificat el document", "Informacio");
            onModificar();
        }

    }

    /**
     * Metode per canviar els noms i les activacions dels botons quan passem al mode de modificacio
     * @throws ExceptionNoSessioActiva Quan no hi ha cap sessio activa
     */
    private void onModificar() throws ExceptionNoSessioActiva {
        modificar = !modificar; // Cert si el podem modificar
        if (modificar) modificarButton.setText("Cancel·la");
        else {
            modificarButton.setText("Modificar");
            if (!Contingut.equals(textPane1.getText()) && ctrlPresentacio.accepta_popup("Aquesta acció comporta perdre les modificacions al contingut")) {
                textPane1.setText(Contingut);
            }
        }
        guardaCanvisButton.setEnabled(modificar);
        textPane1.setEditable(modificar);
        recuperaButton.setEnabled(!modificar);

    }

    /**
     * Cridara a la funcionalitat de recuperar el document
     */
    private void onRecupera() {
        ctrlPresentacio.RecuperaDocument(Autor.getText(), Titol.getText());
    }

    /**
     * Tancara la pestanya. Si hi ha canvis no guardats preguntara abans si els volen guardar
     */
    private void onCancel() {
        if (!Contingut.equals(textPane1.getText()) && modificar) {
            if (ctrlPresentacio.accepta_popup("Aquesta acció comporta perdre les modificacions al contingut")) {
                textPane1.setText(Contingut);
                dispose();
            }
        } else {
            dispose();
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
        contentPane.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, BorderLayout.NORTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Titol = new JLabel();
        Font TitolFont = this.$$$getFont$$$(null, Font.BOLD, 16, Titol.getFont());
        if (TitolFont != null) Titol.setFont(TitolFont);
        Titol.setHorizontalAlignment(2);
        Titol.setText("Títol");
        panel5.add(Titol, BorderLayout.NORTH);
        Autor = new JLabel();
        Autor.setHorizontalAlignment(2);
        Autor.setText("Autor");
        panel5.add(Autor, BorderLayout.CENTER);
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Opcions", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        modificarButton = new JButton();
        modificarButton.setText("Modificar");
        panel6.add(modificarButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guardaCanvisButton = new JButton();
        guardaCanvisButton.setEnabled(false);
        guardaCanvisButton.setText("Guarda Canvis");
        panel6.add(guardaCanvisButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        recuperaButton = new JButton();
        recuperaButton.setText("Recupera");
        panel6.add(recuperaButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel7.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        textPane1 = new JTextPane();
        scrollPane1.setViewportView(textPane1);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Enrere");
        panel9.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        contentPane.add(spacer6, gbc);
    }

    /**
     * inspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * inspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}