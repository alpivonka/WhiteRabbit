package org.ohdsi.rabbitInAHat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ETLDocumentDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox generateETLDocument;
    private JCheckBox generatePseudocodeSql;
    private JCheckBox sourceFillRatesCheckBox;
    private JCheckBox targetFillRatesCheckBox;
    DialogStatus etlDialogStatus = null;

    public ETLDocumentDialog(Window parentWindow){

        super(parentWindow,"ETL Document Generation",ModalityType.MODELESS);
        this.setResizable(false);
        this.setLocation(parentWindow.getX()+parentWindow.getWidth()/2, parentWindow.getY()+100);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

    private void onOK() {
        // add your code here
        etlDialogStatus = new DialogStatus();
        etlDialogStatus.setGenerateETLDocument(generateETLDocument.isSelected());
        etlDialogStatus.setGeneratePseudocodeSql(generatePseudocodeSql.isSelected());
        etlDialogStatus.setSourceFillRates(sourceFillRatesCheckBox.isSelected());
        etlDialogStatus.setTargetFillRates(targetFillRatesCheckBox.isSelected());
        etlDialogStatus.setOk(true);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        etlDialogStatus = new DialogStatus();
        etlDialogStatus.setOk(false);
        dispose();
    }

   /* public static void main(String[] args) {
        ETLDocumentDialog dialog = new ETLDocumentDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
    */

}
