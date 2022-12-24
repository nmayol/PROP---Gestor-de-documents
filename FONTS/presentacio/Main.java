package presentacio;

import Exceptions.ExceptionDirNoTrobat;
import Exceptions.ExceptionDirectoriNoCreat;
import Exceptions.ExceptionDirectoriNoEliminat;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater((new Runnable() {
            @Override
            public void run() {
                CtrlPresentacio cPresentacio = CtrlPresentacio.getInstance();
                try {
                    cPresentacio.iniciPresentacio();
                } catch (ExceptionDirectoriNoCreat | ExceptionDirectoriNoEliminat | ExceptionDirNoTrobat e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
    }
}