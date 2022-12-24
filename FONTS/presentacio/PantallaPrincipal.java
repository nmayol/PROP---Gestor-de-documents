package presentacio;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionIO;
import Exceptions.ExceptionNoSessioActiva;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Aquesta view es la Pantalla Principal del nostre gestor. En aquesta pots veure i realitzar qualsevol operacio
 * i es comunica de manera directa amb el controlador.
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaPrincipal extends JFrame {
    private final CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton TANCAR_SESSIO;
    private JButton OBRIRButton;
    private JButton ELIMINARButton;
    private JButton MODIFICARButton;
    private JButton CARREGARButton;
    private JButton RECUPERARButton;
    private JComboBox<String> JComboCriteris;
    private JPanel VISUALITZADOR_DOCS;
    private JTable DOCUMENTS;
    private JPanel JPanel_TitolTaula;
    private JButton HELPButton;
    private JPanel UNIFICACIO;
    DefaultTableModel def;
    private JPanel Accions;
    private JPanel dades;
    private JLabel titol_taula;
    private JScrollPane JScrollLlistat;
    private JLabel TITOL_PRINCIPAL;
    private JLabel TITOL_CRITERIS;
    private JFormattedTextField TITOL_ACCIONS;

    /**
     * Creadora amb parametres
     *
     * @param docs llistat dels documents de l'usuari
     */
    public PantallaPrincipal(ArrayList<String[]> docs) {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        init_components();
        initButtons();
        init_actions();
        init_dades();
        unificar();
        init_contentPane();
        initPropietats();
        initTable(docs);
        setImatges();
    }

    /**
     * Inicialitzadora de les propietats de la pantalla
     */
    private void initPropietats() {
        setTitle("PANTALLA PRINCIPAL");
        setContentPane(contentPane);
        this.setSize(1200, 800);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    tancar_app();
                } catch (ExceptionNoSessioActiva | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | IOException |
                         ExceptionIO ex) {
                    ex.getMessage();
                }
            }
        });
    }

    /**
     * Inicialitzadora de les imatges de la pantalla
     */
    public void setImatges() {
        ImageIcon myPicture = null;
        try {
            myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/help.png")));
        } catch (IOException e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        Image scaleImage = myPicture.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        myPicture = new ImageIcon(scaleImage);
        HELPButton.setIcon(myPicture);

        try {
            myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/logout.png")));
        } catch (IOException e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        scaleImage = myPicture.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        myPicture = new ImageIcon(scaleImage);
        TANCAR_SESSIO.setIcon(myPicture);
    }

    /**
     * Inicialitzadora dels buttons, les seves propietats i els seus listeners
     */
    public void initButtons() {
        TANCAR_SESSIO = new JButton();
        TANCAR_SESSIO.setText("");
        TANCAR_SESSIO.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    tancar_app();
                } catch (ExceptionNoSessioActiva | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | IOException |
                         ExceptionIO ex) {
                    ctrlPresentacio.accepta_popup(ex.getMessage());
                }
            }
        });

        OBRIRButton = new JButton();
        OBRIRButton.setText("OBRIR");
        OBRIRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_ObrirDocument(event);
                } catch (ExceptionNoSessioActiva e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });

        ELIMINARButton = new JButton();
        ELIMINARButton.setText("ELIMINAR");
        ELIMINARButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_EliminarDocument(event);
                } catch (ExceptionNoSessioActiva e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });

        MODIFICARButton = new JButton();
        MODIFICARButton.setText("MODIFICAR");
        MODIFICARButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_ModificarDocument(event);
                } catch (ExceptionNoSessioActiva e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });

        RECUPERARButton = new JButton();
        RECUPERARButton.setText("RECUPERAR");
        RECUPERARButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_RecuperarDocument(event);
            }
        });

        CARREGARButton = new JButton();
        CARREGARButton.setText("CARREGAR");
        CARREGARButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_CarregarDocument(event);
            }
        });

        HELPButton = new JButton();
        HELPButton.setText("");
        HELPButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_HelPDocument(event);
            }
        });

        RECUPERARButton.setEnabled(false);
    }

    /**
     * Inicialitzadora del JPanel principal
     */
    private void init_contentPane() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(UNIFICACIO, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * Funcio per unificar JPanel de dades i Jpanel d'accions
     */
    private void unificar() {
        final Spacer spacer1 = new Spacer();
        final Spacer spacer2 = new Spacer();

        UNIFICACIO = new JPanel();
        UNIFICACIO.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        UNIFICACIO.add(TANCAR_SESSIO, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UNIFICACIO.add(HELPButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UNIFICACIO.add(TITOL_PRINCIPAL, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        UNIFICACIO.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        UNIFICACIO.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

        UNIFICACIO.add(dades, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * Inicialitzadora dels JComponents de la pantalla
     */
    private void init_components() {
        TITOL_PRINCIPAL = new JLabel();
        TITOL_PRINCIPAL.setText("GESTOR DE DOCUMENTS");

        TITOL_CRITERIS = new JLabel();
        TITOL_CRITERIS.setText("LLISTAR SEGONS CRITERI");

        JComboCriteris = new JComboBox<String>();
        JComboCriteris.setEditable(false);
        JComboCriteris.setEnabled(true);
        final DefaultComboBoxModel<String> defaultComboBoxModel1 = new DefaultComboBoxModel<String>();
        defaultComboBoxModel1.addElement("Prefix");
        defaultComboBoxModel1.addElement("Autor");
        defaultComboBoxModel1.addElement("Semblants");
        defaultComboBoxModel1.addElement("Expressio Booleana");
        defaultComboBoxModel1.addElement("Paraules Clau");
        JComboCriteris.setModel(defaultComboBoxModel1);
        JComboCriteris.setName("criteri");

        TITOL_ACCIONS = new JFormattedTextField();
        TITOL_ACCIONS.setBackground(Color.LIGHT_GRAY);
        TITOL_ACCIONS.setHorizontalAlignment(0);
        TITOL_ACCIONS.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16185079)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        TITOL_ACCIONS.setEditable(false);
        TITOL_ACCIONS.setText("ACCIONS A REALITZAR");


        DOCUMENTS = new JTable();
        DOCUMENTS.setRowHeight(25);
        DOCUMENTS.setRowMargin(3);
        DOCUMENTS.setSelectionBackground(new Color(-12958859));
        DOCUMENTS.setShowVerticalLines(false);

        JScrollLlistat = new JScrollPane();
        JScrollLlistat.setBackground(new Color(-655873));
        JScrollLlistat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16185079)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JScrollLlistat.setViewportView(DOCUMENTS);

        titol_taula = new JLabel();
        titol_taula.setBackground(Color.LIGHT_GRAY);
        titol_taula.setHorizontalAlignment(0);
        titol_taula.setText("LLISTAT DOCUMENTS USUARI");

        JPanel_TitolTaula = new JPanel();
        JPanel_TitolTaula.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        JPanel_TitolTaula.setBackground(Color.LIGHT_GRAY);
        JPanel_TitolTaula.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16185079)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JPanel_TitolTaula.add(titol_taula, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));

        VISUALITZADOR_DOCS = new JPanel();
        VISUALITZADOR_DOCS.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        VISUALITZADOR_DOCS.setFocusable(false);
        VISUALITZADOR_DOCS.add(JScrollLlistat, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        VISUALITZADOR_DOCS.add(JPanel_TitolTaula, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

    }

    /**
     * Inicialitzadora del JPanel de dades
     */
    private void init_dades() {
        dades = new JPanel();
        dades.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        dades.add(Accions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        dades.add(VISUALITZADOR_DOCS, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

    }

    /**
     * Inicialitzadora del JPanel d'accions
     */
    private void init_actions() {
        final Spacer spacer3 = new Spacer();
        final Spacer spacer4 = new Spacer();
        final Spacer spacer5 = new Spacer();
        final Spacer spacer6 = new Spacer();
        final Spacer spacer7 = new Spacer();
        final Spacer spacer8 = new Spacer();
        final Spacer spacer9 = new Spacer();
        final Spacer spacer10 = new Spacer();
        final Spacer spacer11 = new Spacer();

        Accions = new JPanel();
        Accions.setLayout(new GridLayoutManager(15, 3, new Insets(0, 0, 0, 0), -1, -1));
        Accions.setBackground(new Color(-8355710));

        Accions.add(OBRIRButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(ELIMINARButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(MODIFICARButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(CARREGARButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(RECUPERARButton, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(TITOL_CRITERIS, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(JComboCriteris, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        Accions.add(spacer4, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        Accions.add(spacer5, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer6, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer7, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer8, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer9, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer10, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Accions.add(spacer11, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        Accions.add(JComboCriteris, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Accions.add(TITOL_ACCIONS, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));


    }

    /**
     * Inicialitzadora de la taula
     */
    public void initTable(ArrayList<String[]> docs) {
        docs = ctrlPresentacio.ordenar_docs(docs, "titol");
        String[] titol = {"TITOL", "AUTOR", "FORMAT"};

        //ASSSIGNAR TAULA
        def = new DefaultTableModel(null, titol) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < docs.size(); ++i) {
            def.addRow(new String[]{docs.get(i)[0], docs.get(i)[1], String.valueOf(docs.get(i)[2])});
        }

        //ASSIGNAR MODEL
        DOCUMENTS.setModel(def);
        DOCUMENTS.setSelectionBackground(Color.GRAY);


        ListSelectionModel selectionModel = DOCUMENTS.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                RECUPERARButton.setEnabled(selectionModel.getSelectedIndices().length == 1);
            }
        });

        DOCUMENTS.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent mouseEvent) {
                Point point = mouseEvent.getPoint();
                int row = DOCUMENTS.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && DOCUMENTS.getSelectedRow() != -1) {
                    ctrlPresentacio.ObreDocument((String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
                }
            }
        });

        ActionListener act = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((String) JComboCriteris.getSelectedItem() == "Prefix") actionPerformed_LlistarPrefix();
                else if ((String) JComboCriteris.getSelectedItem() == "Autor") {
                    try {
                        actionPerformed_LlistarAutors();
                    } catch (ExceptionNoSessioActiva ex) {
                        ctrlPresentacio.popup(ex.getMessage(), "Error");
                    }
                }
                else if ((String) JComboCriteris.getSelectedItem() == "Semblants") {
                    try {
                        actionPerformed_LlistarSemblants();
                    } catch (ExceptionNoSessioActiva ex) {
                        ctrlPresentacio.popup(ex.getMessage(), "Error");
                    }
                }
                else if ((String) JComboCriteris.getSelectedItem() == "Expressio Booleana")
                    actionPerformed_LlistarExpressio();
                else actionPerformed_LlistarParaulesClau();
            }
        };

        JComboCriteris.addActionListener(act);
    }

    /**
     * Funcio per tancar l'aplicacio
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionDirectoriNoEliminat
     * @throws ExceptionDirNoTrobat
     * @throws IOException
     * @throws ExceptionIO
     */
    public void tancar_app() throws ExceptionNoSessioActiva, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, IOException, ExceptionIO {
        if (ctrlPresentacio.accepta_popup("Vols tancar la sessio?")) ctrlPresentacio.tancarSessio();
    }

    /**
     * Funcio per defecte de la seleccio Autors del JComboBox
     * @throws ExceptionNoSessioActiva
     */
    public void actionPerformed_LlistarAutors() throws ExceptionNoSessioActiva {
        if (DOCUMENTS.getSelectedRow() > -1)
            ctrlPresentacio.setDefaultValues("PantallaDadesAutors", (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
        else ctrlPresentacio.setDefaultValues("PantallaDadesAutors", null, null);
    }

    /**
     * Funcio per defecte de la seleccio Semblants del JComboBox
     * @throws ExceptionNoSessioActiva
     */
    public void actionPerformed_LlistarSemblants() throws ExceptionNoSessioActiva {
        if (DOCUMENTS.getSelectedRow() > -1)
            ctrlPresentacio.setDefaultValues("PantallaDadesSemblants", (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
        else ctrlPresentacio.setDefaultValues("PantallaDadesSemblants", null, null);
    }

    /**
     * Funcio per defecte de la seleccio Prefix del JComboBox
     *
     *
     */
    public void actionPerformed_LlistarPrefix() {
        ctrlPresentacio.controlaVista("PantallaDadesPrefix");
    }

    /**
     * Funcio per defecte de la seleccio Expressio Booleana del JComboBox
     *
     *
     */
    public void actionPerformed_LlistarExpressio() {
        ctrlPresentacio.controlaVista("PantallaLlistarExpressioBooleana");

    }

    /**
     * Funcio per defecte de la seleccio Paraules Clau del JComboBox
     *
     *
     */
    public void actionPerformed_LlistarParaulesClau() {
        ctrlPresentacio.controlaVista("PantallaDadesPClau");
    }

    /**
     * Funcio per defecte del button OBRIRButton
     * @param event
     * @throws ExceptionNoSessioActiva
     */
    public void actionPerformed_ObrirDocument(ActionEvent event) throws ExceptionNoSessioActiva {
        if (DOCUMENTS.getSelectedRow() > -1)
            ctrlPresentacio.setDefaultValues("PantallaDadesObrir", (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
        else ctrlPresentacio.setDefaultValues("PantallaDadesObrir", null, null);
    }

    /**
     * Funcio per defecte del button ELIMINARButton
     * @param event
     * @throws ExceptionNoSessioActiva
     */
    public void actionPerformed_EliminarDocument(ActionEvent event) throws ExceptionNoSessioActiva {
        if (DOCUMENTS.getSelectedRow() > -1)
            ctrlPresentacio.setDefaultValues("PantallaDadesEliminar", (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
        else ctrlPresentacio.setDefaultValues("PantallaDadesEliminar", null, null);
    }

    /**
     * Funcio per defecte del button MODIFICARButton
     * @param event
     * @throws ExceptionNoSessioActiva
     */
    public void actionPerformed_ModificarDocument(ActionEvent event) throws ExceptionNoSessioActiva {
        if (DOCUMENTS.getSelectedRow() > -1)
            ctrlPresentacio.setDefaultValues("PantallaDadesModificar", (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
        else ctrlPresentacio.setDefaultValues("PantallaDadesModificar", null, null);
    }

    /**
     * Funcio per defecte del button RECUPERARButton
     *
     *
     */
    public void actionPerformed_RecuperarDocument(ActionEvent event) {
        ctrlPresentacio.controlaVista("PantallaRecuperarDocument");
    }

    /**
     * Funcio per defecte del button CARREGARButton
     *
     *
     */
    public void actionPerformed_CarregarDocument(ActionEvent event) {
        ctrlPresentacio.controlaVista("PantallaCarregarDocument");
    }

    /**
     * Funcio per defecte del button HELPButton
     *
     *
     */
    public void actionPerformed_HelPDocument(ActionEvent event) {
        ctrlPresentacio.ObreDocument("GRUP 14.4", "MANUAL D'US BASIC DE LA APLICACIO");
    }

    /**
     * Funcio per saber les dades de la fila seleccionada en la taula
     *
     * @return retorna les dades de la fila seleccionada en la taula
     *
     */
    public String[] getFilaSeleccionada() {
        String[] info = new String[]{(String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 2)};
        return info;
    }

}