package presentacio;
import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.xml.sax.SAXException;
import utils.Format;
import utils.KeyP;
import utils.PairP;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Pantalla amb la finestra per carregar un o mes documents.
 *
 * @author Neus Mayol
 */
public class PantallaCarregarDocument extends JDialog {
    //////////////// ESTRUCTURES PANTALLA ////////////////
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<JPanel> LlistaDocus;
    private DefaultListModel<JPanel> Model;
    private JButton CD;
    private JLabel info;
    //////////////////////////////////////////////////////

    /////// ESTRUCTURES PER CARREGAR ELS DOCUMENTS ///////
    // pair<titol,autor>
    Set<KeyP> FitxersPerCarregar; // Serveix per comprovar rapidament que el document no estigui duplicat dins la finestra de carrega
    ArrayList<String> titols;
    ArrayList<String> autors;
    ArrayList<ArrayList<String>> continguts;
    ArrayList<Format> formats;
    //////////////////////////////////////////////////////
    private final CtrlPresentacio ctrlPresentacio;

    /**
     * Creadora, inicialitza la pantalla de carrega.
     */
    public PantallaCarregarDocument() {
        super();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        initInterficie();
        initParametres();
        // Lligam amb CtrlPresentacio
        ctrlPresentacio = CtrlPresentacio.getInstance();


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionDirectoriNoEliminat | ExceptionNoSessioActiva | ExceptionParametresErronis | ExceptionFormatNoValid |
                         ExceptionNotPrimaryKeys | ExceptionDirNoTrobat | ExceptionDocumentJaExisteix | ExceptionIO ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        CD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCarrega();
                } catch (IOException | ExceptionNoSessioActiva | ParserConfigurationException | SAXException |
                         ExceptionCarregaDuplicada ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");

                }
            }
        });

        LlistaDocus.addListSelectionListener(new ListSelectionListener() {
                                                 @Override
                                                 public void valueChanged(ListSelectionEvent e) {
                                                     if (LlistaDocus.getSelectedIndex() != -1 && ctrlPresentacio.accepta_popup("Vols desfer la carrega del fitxer seleccionat?")) {
                                                         int i = LlistaDocus.getSelectedIndex();
                                                         esborraLlista(i);
                                                     }
                                                     if (Model.getSize() == 0) buttonOK.setEnabled(false);

                                                 }
                                             }

        );

        // call onCancel()  when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Funció que cridada quan es vol afegir un nou document la finestra de carrega.
     *
     * @throws IOException                  Saltara si hi ha algun problema en l'escollir o llegir el fitxer
     * @throws ExceptionCarregaDuplicada    Saltara quan un fitxer escollit per carregar te els mateixos identificadors que algun altre fitxer que ja es troba a la finestra de carrega.
     * @throws ExceptionNoSessioActiva      Excepció que saltaria si s'intentes carregar un fitxer sense cap sessió activa.
     * @throws ParserConfigurationException Excepció relativa una mala configuració del parser XML.
     * @throws SAXException                 Excepció relativa un error del parser XML.
     */
    private void onCarrega() throws IOException, ExceptionCarregaDuplicada, ExceptionNoSessioActiva, ParserConfigurationException, SAXException {
        // Creem l'explorador de fitxers
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carrega fitxer");
        // Establim que nomes s'ensenyin els fitxers dels formats que accepta el gestor
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(.txt), (.xml), (.prop)", "txt", "xml", "prop"));

        // Obrir el gestor de fitxers i analitzar la seleccio -> Si se selecciona un fitxer entra a la condició.
        int userSelection = fileChooser.showOpenDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Obtenir el fitxer
            File fileToSave = fileChooser.getSelectedFile();
            assignacio(ctrlPresentacio.tradueixCarrega(fileToSave));
        }
    }

    /**
     * Funció que assigna els valors obtinguts amb la traducció als atributs de la classe que mes tard ens permetra carregar un document.
     *
     * @param traduccio La traducció obtinguda.
     * @throws ExceptionCarregaDuplicada Salta si ja hi ha un document amb els mateixos identificadors a la finestra de carrega.
     * @throws IOException               Si no es pot posar la interficie grafica del nou element a la llista.
     */
    private void assignacio(PairP<KeyP, PairP<Format, ArrayList<String>>> traduccio) throws ExceptionCarregaDuplicada, IOException {
        String titol = traduccio.getFirst().getK1();
        String autor = traduccio.getFirst().getK2();
        ArrayList<String> contingut = traduccio.getSecond().getSecond();
        Format f = traduccio.getSecond().getFirst();


        // Comprovem que no existeix un fitxer amb les mateixes caracteristiques NOMES DINS LA FINESTRA DE CARREGA
        if (!FitxersPerCarregar.contains(traduccio.getFirst())) {

            ///// AFEGIR EL FITXER A LES ESTRUCTURES DE DADES PER CARREGAR /////
            FitxersPerCarregar.add(traduccio.getFirst());
            titols.add(titol);
            autors.add(autor);
            continguts.add(contingut);
            formats.add(f);

            /////// AFEGIR ELEMENTS INTERFICIE ///////
            posarMarc(getFormat(f), titol, autor);

        } else {
            throw new ExceptionCarregaDuplicada();
        }

    }

    /**
     * Inicialitza els parametres de la interficie de la pantalla.
     */
    private void initInterficie() {
        // Inicialització parametres interficie
        LlistaDocus.setCellRenderer(new ImageListCellRenderer());
        LlistaDocus.setLayout(new FlowLayout());
        LlistaDocus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSize(500, 300);
        setLocationRelativeTo(null);
        Model = new DefaultListModel<>();
        LlistaDocus.setModel(Model);
        buttonOK.setEnabled(false);
        setResizable(false);

    }

    /**
     * Inicialitza els parametres que no es veuen per pantalla pero que ens seran imortants per carregggar els documents.
     */
    private void initParametres() {
        // Inicialització de parametres interns
        FitxersPerCarregar = new HashSet<>();
        titols = new ArrayList<>();
        autors = new ArrayList<>();
        continguts = new ArrayList<>();
        formats = new ArrayList<>();

    }

    /**
     * Transforma el valor d'un enum amb en el String corresponent.
     *
     * @param f Conte un dels tres valors de l'enumeració format (txt,xml,prop)
     * @return Retorna en String el format del fitxer.
     */
    private String getFormat(Format f) {
        if (f.equals(Format.xml)) return "xml";
        else if (f.equals(Format.txt)) return "txt";
        else return "prop";
    }


    /**
     * Funció per crear el marc on anira un nou element de la llista quan seleccionem aquest per que sigui carregat.
     *
     * @param format Format del fitxer
     * @param titol  Titol del fitxer
     * @param autor  Autor del fitxer
     * @throws IOException Salta si no es llegeix be el fitxer.
     */
    private void posarMarc(String format, String titol, String autor) throws IOException {
        ///// FER EL FITXER VISIBLE DES DE LA INTERFICIE /////

        // Agafar la imatge de la icona i tractar-la
        ImageIcon myPicture = new ImageIcon(ImageIO.read(new File("FONTS/presentacio/Imagen/" + format + ".png")));

        // Crear el nou recuadre i afegir-hi la iconab el titol i l'autor
        JPanel marc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        marc.setSize(200, 200);
        marc.add(new JLabel(titol, myPicture, JLabel.LEFT));
        JLabel autorMarc = new JLabel(" by: " + autor);
        autorMarc.setForeground(Color.GRAY);
        marc.add(autorMarc);

        Model.addElement(marc);
        LlistaDocus.setModel(Model);
        LlistaDocus.setVisible(true);
        buttonOK.setEnabled(true);
    }


    /**
     * Ordena la carrega de tots els documents de la finestra de carrega.
     *
     * @throws ExceptionNoSessioActiva     En cas que s'intentes carregar un document quan la sessió no esta activada.
     * @throws ExceptionParametresErronis  En cas que els parametres siguin erronis.
     * @throws ExceptionFormatNoValid      Quan el format demanat per carregar no es un dels tres que accepta el gestor.
     * @throws ExceptionDocumentJaExisteix Quan un dels documents del gestor ja te els mateixos identificadors.
     * @throws ExceptionNotPrimaryKeys     Quan falten les claus primaries d'un dels documents.
     */
    private void onOK() throws ExceptionNoSessioActiva, ExceptionParametresErronis, ExceptionFormatNoValid, ExceptionDocumentJaExisteix, ExceptionNotPrimaryKeys, ExceptionDirNoTrobat, ExceptionIO, ExceptionDirectoriNoEliminat {
        ctrlPresentacio.carregarDocuments(titols, autors, formats, continguts);
        dispose();
    }

    /**
     * Ordena tancar la pestanya de carrega.
     */
    private void onCancel() {
        // add your code here if necessary
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
     * inspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Carrega");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        CD = new JButton();
        CD.setText("Afegeix un document");
        panel3.add(CD, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        LlistaDocus = new JList();
        LlistaDocus.putClientProperty("List.isFileList", Boolean.FALSE);
        scrollPane1.setViewportView(LlistaDocus);
        info = new JLabel();
        info.setText("Afegeix el(s) document(s) que vulguis carregar i apreta OK per afegir-los al gestor.");
        contentPane.add(info, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * inspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    /**
     * Es una classe auxiliar que ens permet posar imatges dins la llista de documents a carregar. La farem servir per posar les icones a la llista de carregar.
     *
     * @author Neus Mayol
     */
    class ImageListCellRenderer implements ListCellRenderer {

        /**
         * retorna un ListCellRenderer
         *
         * @param jlist        La llista que pintem.
         * @param value        The value returned by list.getModel().getElementAt(index).
         * @param cellIndex    L'index de la cella a la llista.
         * @param isSelected   Cert si la cella en questio esta selleccionada.
         * @param cellHasFocus True if the specified cell has the focus.
         * @return un ListCellRendererComponent amb color de fons demanat per la llista.
         */
        public Component getListCellRendererComponent(JList jlist, Object value, int cellIndex, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof JPanel) {
                Component component = (Component) value;
                component.setForeground(Color.white);
                component.setBackground(isSelected ? Color.LIGHT_GRAY : Color.white);
                return component;
            } else {
                // TODO - I get one String here when the JList is first rendered; proper way to deal with this?
                return new JLabel("???");
            }
        }
    }


    /**
     * Esborra un fitxer de la llista de carrega
     *
     * @param i Es l'index a la llista del fitxer que volem eliminar
     */
    private void esborraLlista(int i) {
        String t = titols.get(i), a = autors.get(i);
        FitxersPerCarregar.remove(new KeyP(t, a));
        titols.remove(i);
        autors.remove(i);
        continguts.remove(i);
        formats.remove(i);
        Model.removeElementAt(i);
        LlistaDocus.setSelectedIndex(-1);
        LlistaDocus.setModel(Model);
        LlistaDocus.setVisible(true);
    }


}
