package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla per poder iniciar sessio usuari
 */
public class PantallaIniciarSessio extends JFrame {
    private final CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton iniciarSessioButton;
    private JButton registraTButton;
    private JPasswordField passwordField1;
    private JTextField Usuari;
    private JLabel nomUsuari;
    private JLabel contrasenya;
    private JButton eliminarUsuariButton;
    private JPanel botoIniciarSessio;
    private JPanel espaiatUsuCntr;
    private JLabel gestorDocuments;
    private JLabel prop;
    private JPanel textPanel;
    private JLabel LabelFoto;
    private JPanel PanelFoto;

    /**
     * Creadora per defecte
     */
    public PantallaIniciarSessio() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        this.setTitle("Iniciar sessió usuari");

        setContentPane(contentPane);
        getRootPane().setDefaultButton(iniciarSessioButton);

        // MIDA I LLETRA DELS BOTONS
        iniciarSessioButton.setSize(200, 30);
        registraTButton.setSize(50, 30);
        eliminarUsuariButton.setSize(50, 30);
        Font botons = new Font(null, Font.PLAIN, 15);
        iniciarSessioButton.setFont(botons);
        registraTButton.setFont(botons);
        eliminarUsuariButton.setFont(botons);

        // FONT NOMS
        Font noms = new Font(null, Font.PLAIN, 20);
        nomUsuari.setFont(noms);
        contrasenya.setFont(noms);
        // FONT TITOLS
        prop.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 45));
        gestorDocuments.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));

        // MIDA DE LA PANTALLA
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int amplada = (int) Math.round(screenSize.width * 0.85);
        int alcada = (int) Math.round(screenSize.height * 0.85);
        this.setSize(amplada, alcada);
        setResizable(true);
        setLocationRelativeTo(null);

        iniciarSessioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_EntrarGestor(event);
                } catch (ExceptionUsuariNoExisteix | ExceptionContrasenyaIncorrecta | ExceptionSessioJaActiva |
                         ExceptionDocumentJaExisteix | ExceptionDirNoTrobat | ExceptionCampsBuits | ExceptionIO |
                         ExceptionDirectoriNoEliminat e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });

        registraTButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_PantallaRegistrarUsuari(event);
            }
        });

        eliminarUsuariButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_PantallaEliminarUsuari(event);
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    onCancel();
                } catch (ExceptionNoSessioActiva | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | ExceptionIO ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCancel();
                } catch (ExceptionNoSessioActiva | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | ExceptionIO ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Funcio per defecte del boto iniciar sessio.
     * Si s'han introduit els parametres correctament, s'entrara a la Pantalla Principal del gestor
     * @throws ExceptionUsuariNoExisteix Salta quan el nom de l'usuari que volem afegir no existeix
     * @throws ExceptionContrasenyaIncorrecta Salta quan la contrasenya de l'usuari es incorrecte
     * @throws ExceptionSessioJaActiva Salta quan ja hi ha una sessio dun altre usuari iniciada
     * @throws ExceptionDirNoTrobat Salta quan no s'ha pogut iniciar sessio de l'usuari correctament
     * @throws ExceptionCampsBuits Salta quan algun dels camps estan buits
     * @throws ExceptionIO Salta quan hi ha algun error en la persistencia
     */
    private void actionPerformed_EntrarGestor(ActionEvent event) throws ExceptionUsuariNoExisteix, ExceptionContrasenyaIncorrecta, ExceptionSessioJaActiva, ExceptionDocumentJaExisteix, ExceptionDirNoTrobat, ExceptionCampsBuits, ExceptionIO, ExceptionDirectoriNoEliminat {
        String usuari = Usuari.getText();
        String password = String.valueOf(passwordField1.getPassword());
        ctrlPresentacio.iniciarSessio(usuari, password);
        this.dispose();
        ctrlPresentacio.controlaVista("PantallaPrincipal");
    }

    /**
     * Funcio per defecte del boto registra't.
     * Obre la Pantalla Registrar Usuari.
     */
    private void actionPerformed_PantallaRegistrarUsuari(ActionEvent event) {
        this.dispose();
        ctrlPresentacio.controlaVista("PantallaRegistrarUsuari");
    }

    /**
     * Funcio per defecte del boto eliminar usuari.
     * Obre la Pantalla Eliminar Usuari.
     */
    private void actionPerformed_PantallaEliminarUsuari(ActionEvent event) {
        this.dispose();
        ctrlPresentacio.controlaVista("PantallaEliminarUsuaris");
    }

    /**
     * Funcio per defecte per tancar l'aplicacio
     * @throws ExceptionDirectoriNoEliminat Salta si hi ha un problema a persistencia
     * @throws ExceptionDirNoTrobat Salta si hi ha un problema a persistencia
     * @throws ExceptionIO Salta si hi ha un problema a persistencia
     */
    private void onCancel() throws ExceptionNoSessioActiva, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO {
        // add your code here if necessary
        ctrlPresentacio.tancarAplicacio();
        dispose();
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
        contentPane.setLayout(new GridLayoutManager(9, 3, new Insets(10, 10, 10, 10), -1, -1));
        gestorDocuments = new JLabel();
        gestorDocuments.setText("GESTOR DOCUMENTS");
        contentPane.add(gestorDocuments, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(10, 10), null, new Dimension(20, 20), 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        prop = new JLabel();
        prop.setText("PROJECTES DE PROGRAMACIÓ:");
        contentPane.add(prop, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textPanel = new JPanel();
        textPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(textPanel, new GridConstraints(7, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomUsuari = new JLabel();
        nomUsuari.setText("Nom usuari:");
        textPanel.add(nomUsuari, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Usuari = new JTextField();
        Usuari.setText("");
        textPanel.add(Usuari, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(150, -1), null, 0, false));
        espaiatUsuCntr = new JPanel();
        espaiatUsuCntr.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        textPanel.add(espaiatUsuCntr, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        espaiatUsuCntr.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 7), new Dimension(-1, 7), new Dimension(-1, 7), 0, false));
        contrasenya = new JLabel();
        contrasenya.setText("Contrasenya:");
        espaiatUsuCntr.add(contrasenya, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField1 = new JPasswordField();
        espaiatUsuCntr.add(passwordField1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(150, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        espaiatUsuCntr.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), new Dimension(-1, 20), new Dimension(-1, 20), 0, false));
        botoIniciarSessio = new JPanel();
        botoIniciarSessio.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        espaiatUsuCntr.add(botoIniciarSessio, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        botoIniciarSessio.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        botoIniciarSessio.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        iniciarSessioButton = new JButton();
        iniciarSessioButton.setText("Iniciar Sessio");
        botoIniciarSessio.add(iniciarSessioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        registraTButton = new JButton();
        registraTButton.setText("Registra't");
        contentPane.add(registraTButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eliminarUsuariButton = new JButton();
        eliminarUsuariButton.setText("Eliminar usuari");
        contentPane.add(eliminarUsuariButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(8, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        PanelFoto = new JPanel();
        PanelFoto.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(PanelFoto, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LabelFoto = new JLabel();
        LabelFoto.setText("");
        PanelFoto.add(LabelFoto, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
