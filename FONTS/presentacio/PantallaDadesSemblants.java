package presentacio;

import Exceptions.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Aquesta view demana les dades a l'usuari per tal de poder llistar document/s segons la semblança al document indicat
 * No te estructures de dades, pero n'envia cap al controlador per a que aquest les pugui utilitzar.
 */
public class PantallaDadesSemblants extends JFrame {
    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner K;
    private JRadioButton QUANTITATDEPARAULESSEMBLANTSRadioButton;
    private JRadioButton PERVALORDELSPESOSRadioButton;
    private JComboBox<String> autors;
    private JComboBox<String> titols;
    private JPanel confirm;
    private JPanel data;

    /**
     * Creadora per defecte
     */
    public PantallaDadesSemblants() {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        initButtons();
        init_confirm();
        init_dades(null, null);
        init_contentPane();
        initPropietats();
    }

    public PantallaDadesSemblants(String DEF_Autor, String DEF_Titol) {
        super();
        ctrlPresentacio = CtrlPresentacio.getInstance();

        initButtons();
        init_confirm();
        init_dades(DEF_Autor, DEF_Titol);
        init_contentPane();
        initPropietats();

        if (DEF_Titol != null) titols.setSelectedItem(DEF_Titol);
        else titols.setSelectedItem("");
        if (DEF_Autor != null) autors.setSelectedItem(DEF_Autor);
        else autors.setSelectedItem("");
    }

    /**
     * Inicialitzadora dels buttons, les seves propietats i els seus listeners
     */
    private void initButtons() {
        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");

        PERVALORDELSPESOSRadioButton = new JRadioButton();
        PERVALORDELSPESOSRadioButton.setText("VALOR DELS PESOS (IDF)");
        QUANTITATDEPARAULESSEMBLANTSRadioButton = new JRadioButton();
        QUANTITATDEPARAULESSEMBLANTSRadioButton.setText("QUANTITAT DE PARAULES SEMBLANTS");


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionNoEnterValid | ExceptionDocumentNoExisteix | ExceptionNoSessioActiva |
                         ExceptionConsultaLimitParametres | ExceptionAutorNoExisteix | ExceptionDirNoTrobat |
                         ExceptionIO | ExceptionCampsBuits ex) {
                    ctrlPresentacio.popup(ex.getMessage(), "Error");
                }
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_TornarEnrere(event);
            }
        });
        PERVALORDELSPESOSRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    QUANTITATDEPARAULESSEMBLANTSRadioButton.setSelected(false);
                }
            }
        });
        QUANTITATDEPARAULESSEMBLANTSRadioButton.addItemListener(new ItemListener() {
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
     * Inicialitzadora de les propietats de la pantalla
     *
     *
     */
    private void initPropietats() {
        setTitle("DADES PER LLISTAR DOCUMENTS SEMBLANTS");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(550, 275);
        setLocationRelativeTo(null);

        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(0);
        K.setModel(model);


        DefaultComboBoxModel<String> a = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> t = new DefaultComboBoxModel<>();

        ArrayList<String[]> data = null;
        try {
            data = ctrlPresentacio.getSemblantsHistorial();
        } catch (ExceptionNoSessioActiva e) {
            ctrlPresentacio.popup(e.getMessage(), "Error");
        }
        for (int i = 0; i < data.size(); ++i){
            a.addElement(data.get(i)[0]);
            t.addElement(data.get(i)[1]);
        }

        autors.setModel(a);
        titols.setModel(t);

        autors.setSelectedItem("");
        titols.setSelectedItem("");
    }

    /**
     * Funcio per defecte del button buttonOk
     * S'encarrega de continuar amb el flux de l'accio (llistar documents semblants) passant-ne com a parametre
     * el valor escrit en el JComboBox titol, el valor escrit en el JComboBox autor i l'enter inidicat en JSpinner K.
     * @throws ExceptionNoEnterValid
     * @throws ExceptionDocumentNoExisteix
     * @throws ExceptionNoSessioActiva
     * @throws ExceptionConsultaLimitParametres
     * @throws ExceptionAutorNoExisteix
     * @throws ExceptionDirNoTrobat
     * @throws ExceptionIO
     * @throws ExceptionCampsBuits
     */
    public void onOK() throws ExceptionNoEnterValid, ExceptionDocumentNoExisteix, ExceptionNoSessioActiva, ExceptionConsultaLimitParametres, ExceptionAutorNoExisteix, ExceptionDirNoTrobat, ExceptionIO, ExceptionCampsBuits {
        if (PERVALORDELSPESOSRadioButton.isSelected())
            ctrlPresentacio.llistarDocumentsSemblants((String) autors.getSelectedItem(), (String) titols.getSelectedItem(), (int) K.getValue(), "pesos");
        else
            ctrlPresentacio.llistarDocumentsSemblants((String) autors.getSelectedItem(), (String) titols.getSelectedItem(), (int) K.getValue(), "bool");
        this.dispose();
    }

    /**
     * Funcio per defecte del button buttonCancel
     * Tanca la pantalla
     *
     *
     */
    public void actionPerformed_TornarEnrere(ActionEvent event) {
        this.dispose();
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
        final Spacer spacer1 = new Spacer();
        confirm.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        confirm.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }

    /**
     * Inicialitzadora del JPanel de dades
     *
     *
     */
    private void init_dades(String DEF_Autor, String DEF_Titol) {
        data = new JPanel();
        data.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("AUTOR:");
        data.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("TITOL:");
        data.add(label2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("NUMERO");
        data.add(label3, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        K = new JSpinner();
        data.add(K, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        data.add(PERVALORDELSPESOSRadioButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        data.add(QUANTITATDEPARAULESSEMBLANTSRadioButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("CRITERI DE SEMBLANÇA");
        data.add(label4, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autors = new JComboBox<String>();
        autors.setEditable(true);
        autors.setSelectedItem(DEF_Autor);
        data.add(autors, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titols = new JComboBox<String>();
        titols.setEditable(true);
        titols.setSelectedItem(DEF_Titol);
        data.add(titols, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }
}