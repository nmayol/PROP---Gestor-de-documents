package presentacio;

import javax.swing.*;
import java.awt.event.*;


import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Aquesta view ens permet llistar els documents segons un criteri
 */
public class PantallaLlistar extends JFrame {
    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton HOME;
    private JButton ENRERE;
    private JTable DOCUMENTS;
    private JButton OBRIRButton;
    private JPanel Llistar;
    private JComboBox<String> ordenacio;
    private JButton ELIMINARButton;
    private JSpinner K;
    private JLabel PREVISUALITZACIO;
    private JLabel MENU;
    private JPanel ACCIONS;
    private JTextField TITOL_DOC;
    private JTextField AUTOR_DOC;
    private JButton LLISTARSEMBLANTSButton;
    private JPanel DOC_IMAGE;
    private JRadioButton PERVALORDELSPESOSRadioButton;
    private JRadioButton PERQUANTITATDEPARAULESRadioButton;
    private ArrayList<String[]> docs_a_llistar;
    private JPanel INFO_DOC;
    private JTextArea CONTINGUT_DOC;
    private JPanel data;
    DefaultTableModel model;
    String t;

    /**
     * Constructora
     * @param docs conjunt de documents
     * @param tipus ens indica el tipus de llistat que vol l'usuari
     */
    public PantallaLlistar(ArrayList<String[]> docs, String tipus) {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        if (tipus.equals("AUTOR")) setTitle("PANTALLA CERQUES DOCUMENTS AUTOR");
        else if (tipus.equals("SEMBLANTS")) setTitle("PANTALLA CERQUES DOCUMENTS SEMBLANTS");
        else if (tipus.equals("PARAULES CLAU")) setTitle("PANTALLA CERQUES DOCUMENTS PARAULES CLAU");

        setContentPane(contentPane);
        getRootPane().setDefaultButton(HOME);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        contentPane.setBackground(Color.LIGHT_GRAY);
        docs_a_llistar = docs;
        initButtons(tipus);
        initComponents();
        initImatges();
        initPropietats();

        t = tipus;
    }

    /**
     * Incialitzadora de les propietats
     *
     */
    private void initPropietats() {
        DOC_IMAGE.setLayout(new FlowLayout());
        MENU.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        PREVISUALITZACIO.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        CONTINGUT_DOC.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        TITOL_DOC.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        AUTOR_DOC.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(0);
        K.setModel(model);
    }


    /**
     * Inicialitzadora de la JTable
     *
     */
    private void initTable() {
        String[] titol = {"TITOL", "AUTOR", "FORMAT"};

        //ASSSIGNAR TAULA
        model = new DefaultTableModel(null, titol) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < docs_a_llistar.size(); ++i) {
            model.addRow(new String[]{docs_a_llistar.get(i)[0], docs_a_llistar.get(i)[1], String.valueOf(docs_a_llistar.get(i)[2])});
        }

        //ASSIGNAR MODEL
        DOCUMENTS.setModel(model);

        DOCUMENTS.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
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
                if ((String) ordenacio.getSelectedItem() == "titol") {
                    docs_a_llistar = ctrlPresentacio.ordenar_docs(docs_a_llistar, "titol");
                    initTable();
                } else {
                    docs_a_llistar = ctrlPresentacio.ordenar_docs(docs_a_llistar, "modificacio");
                    initTable();
                }
            }
        };

        ordenacio.addActionListener(act);
    }

    /**
     * Incialitzadora dels JComponents
     *
     */
    private void initComponents() {
        docs_a_llistar = ctrlPresentacio.ordenar_docs(docs_a_llistar, "titol");
        initTable();

        DOCUMENTS.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (DOCUMENTS.getSelectedRow() > -1) {
                    TITOL_DOC.setText(docs_a_llistar.get(DOCUMENTS.getSelectedRow())[0]);
                    AUTOR_DOC.setText(docs_a_llistar.get(DOCUMENTS.getSelectedRow())[1]);
                    ArrayList<String> c = null;
                    try {
                        c = ctrlPresentacio.getContingutDocument((String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1),
                                (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
                        String d2 = String.join(" ", c);
                        CONTINGUT_DOC.setText(d2);
                    } catch (ExceptionNoSessioActiva | ExceptionDocumentNoExisteix | ExceptionCampsBuits e) {
                        ctrlPresentacio.popup(e.getMessage(), "Error");
                    }
                    setImatges();
                }
            }
        });


    }


    /**
     * Incialitzadora de les imatges de la pantalla per previsualitzar documents
     *
     */
    private void setImatges() {
        DOC_IMAGE.removeAll();
        DOC_IMAGE.repaint();
        ImageIcon myPicture = null;
        try {
            myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/" + docs_a_llistar.get(DOCUMENTS.getSelectedRow())[2] + ".png")));
        } catch (IOException e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        Image scaleImage = myPicture.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        myPicture = new ImageIcon(scaleImage);

        // Crear el nou recuadre i afegir-hi la iconab el titol i l'autor
        JPanel marc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        marc.setSize(200, 200);
        marc.add(new JLabel(docs_a_llistar.get(DOCUMENTS.getSelectedRow())[0], myPicture, JLabel.LEFT));
        JLabel autorMarc = new JLabel(" by: " + docs_a_llistar.get(DOCUMENTS.getSelectedRow())[1]);
        autorMarc.setForeground(Color.GRAY);
        marc.add(autorMarc);
        DOC_IMAGE.add(marc);
    }


    /**
     * Incialitzadora dels Buttons de la pantalla
     *
     */
    private void initButtons(String tipus) {
        HOME.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_TornarMenu(event);
            }
        });
        ENRERE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_TornarEnrere(event);
            }
        });
        OBRIRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_ObrirDocument(event);
            }
        });
        ELIMINARButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_EliminarDocument(event);
                } catch (ExceptionDocumentNoExisteix | ExceptionNoSessioActiva | ExceptionAutorNoExisteix |
                         ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat | ExceptionIO | IOException |
                         ExceptionCampsBuits e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });
        LLISTARSEMBLANTSButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_LlistarSemblants(event);
                } catch (ExceptionNoEnterValid | ExceptionDocumentNoExisteix | ExceptionNoSessioActiva |
                         ExceptionConsultaLimitParametres | ExceptionAutorNoExisteix | ExceptionDirNoTrobat |
                         ExceptionIO | ExceptionCampsBuits e) {
                    ctrlPresentacio.popup(e.getMessage(), "Error");
                }
            }
        });
        PERVALORDELSPESOSRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    PERQUANTITATDEPARAULESRadioButton.setSelected(false);
                }
            }
        });
        PERQUANTITATDEPARAULESRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    PERVALORDELSPESOSRadioButton.setSelected(false);
                }
            }
        });

        PERVALORDELSPESOSRadioButton.setSelected(true);
    }


    /**
     * Incialitzadora de les propietats de les imatges
     *
     */
    private void initImatges() {
        ImageIcon myPicture = null;
        try {
            myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/home.png" +
                    "")));
        } catch (IOException e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }

        Image scaleImage = myPicture.getImage().getScaledInstance(30, 20, Image.SCALE_DEFAULT);
        myPicture = new ImageIcon(scaleImage);
        HOME.setIcon(myPicture);

        try {
            myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/back.png" +
                    "")));
        } catch (IOException e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }

        scaleImage = myPicture.getImage().getScaledInstance(30, 20, Image.SCALE_DEFAULT);
        myPicture = new ImageIcon(scaleImage);
        ENRERE.setIcon(myPicture);
    }


    /**
     * Funció per defecte de LlistarSemblants
     *
     */
    private void actionPerformed_LlistarSemblants(ActionEvent event) throws ExceptionNoEnterValid, ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if (PERVALORDELSPESOSRadioButton.isSelected())
            ctrlPresentacio.llistarDocumentsSemblants(AUTOR_DOC.getText(), TITOL_DOC.getText(), (int) K.getValue(), "pesos");
        else if (PERQUANTITATDEPARAULESRadioButton.isSelected())
            ctrlPresentacio.llistarDocumentsSemblants(AUTOR_DOC.getText(), TITOL_DOC.getText(), (int) K.getValue(), "bool");
        dispose();
    }

    /**
     * Funció per defecte de ELIMINARButton
     *
     */
    private void actionPerformed_EliminarDocument(ActionEvent event) throws ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionAutorNoExisteix, ExceptionDirectoriNoEliminat, ExceptionDirNoTrobat, ExceptionIO, IOException, ExceptionCampsBuits {
        docs_a_llistar.remove(DOCUMENTS.getSelectedRow());
        ctrlPresentacio.eliminar_doc(AUTOR_DOC.getText(), TITOL_DOC.getText());
        model.removeRow(DOCUMENTS.getSelectedRow());
        DOC_IMAGE.removeAll();
    }

    /**
     * Funció per defecte de OBRIRButton
     *
     */
    private void actionPerformed_ObrirDocument(ActionEvent event) {
        ctrlPresentacio.ObreDocument((String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 1), (String) DOCUMENTS.getValueAt(DOCUMENTS.getSelectedRow(), 0));
    }

    /**
     * Funció per defecte de HOMEButton
     *
     */
    public void actionPerformed_TornarMenu(ActionEvent event) {
        ctrlPresentacio.controlaVista("PantallaPrincipal");
        this.dispose();
    }

    /**
     * Funció per defecte de ENREREButton
     *
     */
    public void actionPerformed_TornarEnrere(ActionEvent event) {
        this.dispose();
        ctrlPresentacio.controlaVista("PantallaPrincipal");
        if (t.equals("SEMBLANTS")) ctrlPresentacio.controlaVista("PantallaDadesSemblants");
        else if (t.equals("PARAULES CLAU")) ctrlPresentacio.controlaVista("PantallaDadesPClau");
        else if (t.equals("AUTOR")) ctrlPresentacio.controlaVista("PantallaDadesAutors");
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }


    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 6, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("criteri ordenacio:");
        contentPane.add(label1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ordenacio = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("titol");
        defaultComboBoxModel1.addElement("modifiacio");
        ordenacio.setModel(defaultComboBoxModel1);
        contentPane.add(ordenacio, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ENRERE = new JButton();
        ENRERE.setText("");
        contentPane.add(ENRERE, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        HOME = new JButton();
        HOME.setText("");
        contentPane.add(HOME, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        data = new JPanel();
        data.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(data, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Llistar = new JPanel();
        Llistar.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        Llistar.setFocusable(false);
        data.add(Llistar, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        Llistar.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16185079)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        DOCUMENTS = new JTable();
        Font DOCUMENTSFont = this.$$$getFont$$$("Arial Black", -1, 12, DOCUMENTS.getFont());
        if (DOCUMENTSFont != null) DOCUMENTS.setFont(DOCUMENTSFont);
        DOCUMENTS.setRowMargin(3);
        DOCUMENTS.setShowVerticalLines(false);
        scrollPane1.setViewportView(DOCUMENTS);
        ACCIONS = new JPanel();
        ACCIONS.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        data.add(ACCIONS, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        ACCIONS.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        MENU = new JLabel();
        MENU.setText("   MENU ACCIONS RAPIDES");
        panel1.add(MENU, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        INFO_DOC = new JPanel();
        INFO_DOC.setLayout(new GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(INFO_DOC, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        INFO_DOC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        PREVISUALITZACIO = new JLabel();
        PREVISUALITZACIO.setText("   PREVISUALITZACIO");
        INFO_DOC.add(PREVISUALITZACIO, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 10, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("TITOL DEL DOCUMENT:");
        INFO_DOC.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, -1, 10, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("AUTOR DEL DOCUMENT:");
        INFO_DOC.add(label3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final Spacer spacer3 = new Spacer();
        INFO_DOC.add(spacer3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, -1, 10, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("CONTINGUT DEL DOCUMENT:");
        INFO_DOC.add(label4, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        CONTINGUT_DOC = new JTextArea();
        CONTINGUT_DOC.setEditable(false);
        CONTINGUT_DOC.setText("");
        INFO_DOC.add(CONTINGUT_DOC, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(650, 200), new Dimension(650, 200), new Dimension(650, 200), 0, false));
        TITOL_DOC = new JTextField();
        TITOL_DOC.setEditable(false);
        INFO_DOC.add(TITOL_DOC, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 1, false));
        AUTOR_DOC = new JTextField();
        AUTOR_DOC.setEditable(false);
        INFO_DOC.add(AUTOR_DOC, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 1, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        OBRIRButton = new JButton();
        OBRIRButton.setText("OBRIR ");
        panel3.add(OBRIRButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        LLISTARSEMBLANTSButton = new JButton();
        LLISTARSEMBLANTSButton.setText("LLISTAR SEMBLANTS");
        panel3.add(LLISTARSEMBLANTSButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PERVALORDELSPESOSRadioButton = new JRadioButton();
        PERVALORDELSPESOSRadioButton.setText("PER VALOR DELS PESOS (IDF)");
        panel3.add(PERVALORDELSPESOSRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("CRITERI DE SEMBLANÇA");
        panel3.add(label5, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        PERQUANTITATDEPARAULESRadioButton = new JRadioButton();
        PERQUANTITATDEPARAULESRadioButton.setText("PER PARAULES SEMBLANTS");
        panel3.add(PERQUANTITATDEPARAULESRadioButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ELIMINARButton = new JButton();
        ELIMINARButton.setText("ELIMINAR");
        panel3.add(ELIMINARButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        K = new JSpinner();
        panel3.add(K, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        DOC_IMAGE = new JPanel();
        DOC_IMAGE.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(DOC_IMAGE, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }


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


    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}