package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PantallaRecuperarDocument extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JLabel Ruta;
    private JScrollPane recuadreRuta;
    String Path = "";
    String Nom = "";

    // Dades document a recuperar
    String Titol;
    String Autor;
    ArrayList<String> Contingut;

    CtrlPresentacio ctrlPresentacio;

    public PantallaRecuperarDocument(String a, String t) {
        // Lligam amb CtrlPresentacio
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        Autor = a;
        Titol = t;

        setContentPane(contentPane);
        setModal(true);
        setSize(450, 175);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Recuperar document");
        setLocationRelativeTo(null);
        buttonOK.setEnabled(false);
        Ruta.setForeground(Color.GRAY);
        Ruta.setFont(new Font("Consolas", Font.PLAIN, 12));
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException | ExceptionCampsBuits | ExceptionDirNoTrobat | ExceptionDocumentNoExisteix | ExceptionNoSessioActiva ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onAdd();
                } catch (ExceptionSobreescriureFitxer ex) {
                    if (ctrlPresentacio.accepta_popup("Esta segur que vol sobreescriure el fitxer " + Nom + '?')) ;
                    else Ruta.setText("");
                } catch (IOException ex) {
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

    //  TODO Hem de poder buscar passar l'extensio del fitxer aqui per poder-li dir al explorador que nomes ens mostri els fitxer d'aquell format.
    private void onAdd() throws ExceptionSobreescriureFitxer, IOException {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(.txt) (.xml) (.prop)", "txt", "xml", "prop"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(.txt)", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(.xml)", "xml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(.prop)", "prop"));
        fileChooser.setDialogTitle("Especifica la ruta on vols guardar el fitxer");
        fileChooser.setSelectedFile(new File(Titol + " - " + Autor));

        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            Path = ((File) fileToSave).getAbsolutePath();
            if (!Path.endsWith(".txt") && !Path.endsWith(".xml") && !Path.endsWith(".prop")) {
                Path = Path + ".txt";
                fileToSave = new File(Path + ".txt");
            }
            Nom = fileToSave.getName();
            Ruta.setText(Path);
            buttonOK.setEnabled(true);
            if (fileToSave.exists()) {
                throw new ExceptionSobreescriureFitxer();
            }
        }
        setVisible(true);
    }


    private void onOK() throws IOException, ExceptionNoSessioActiva, ExceptionDirNoTrobat, ExceptionDocumentNoExisteix, ExceptionCampsBuits {
        if (Path.equals("")) System.out.println("S'ha d'especificar la ruta on es vol guardar el fitxer.");
        else {
            Contingut = ctrlPresentacio.getContingutDocument(Autor, Titol);
            ctrlPresentacio.GuardaDocument(Path, Autor, Titol, Contingut);
            dispose();

        }
    }

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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setMaximumSize(new Dimension(-1, -1));
        contentPane.setMinimumSize(new Dimension(-1, -1));
        contentPane.setPreferredSize(new Dimension(-1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Tria el directori on vols guardar el fitxer");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAdd = new JButton();
        buttonAdd.setText("Navega...");
        panel4.add(buttonAdd, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        recuadreRuta = new JScrollPane();
        recuadreRuta.setVerticalScrollBarPolicy(21);
        panel3.add(recuadreRuta, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Ruta = new JLabel();
        Ruta.setBackground(new Color(-12301737));
        Ruta.setText("");
        recuadreRuta.setViewportView(Ruta);
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * inspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}