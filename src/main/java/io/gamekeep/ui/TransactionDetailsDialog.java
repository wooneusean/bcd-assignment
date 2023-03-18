package io.gamekeep.ui;

import com.github.lgooddatepicker.components.DateTimePicker;
import io.gamekeep.components.Transaction;
import io.gamekeep.ui.documentfilters.NumberDocumentFilter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

public class TransactionDetailsDialog extends JDialog {

    private final Transaction transaction;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtBuyerId;
    private JTextField txtTransactionId;
    private JTextField txtSignature;
    private JTextField txtSellerId;
    private JTextField txtAmountPaid;
    private JTextField txtLicenseCode;
    private JTextField txtPublisherId;
    private JTextField txtGameId;
    private DateTimePicker dtpTransactionDate;

    public TransactionDetailsDialog(Transaction transaction) {
        this.transaction = transaction;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("New Transaction");
        pack();

        if (transaction.getTransactionDate().isBefore(LocalDateTime.now().minusSeconds(1))) {
            setTitle("Transaction - " + transaction.getTransactionId());
            Arrays.stream(new JTextComponent[]{
                    txtBuyerId,
                    txtTransactionId,
                    txtSignature,
                    txtSellerId,
                    txtAmountPaid,
                    txtLicenseCode,
                    txtPublisherId,
                    txtGameId
            }).forEach(c -> c.setEditable(false));

            dtpTransactionDate.setEnabled(false);
        }

        ((PlainDocument) txtAmountPaid.getDocument()).setDocumentFilter(new NumberDocumentFilter());

        txtAmountPaid.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                if (txtAmountPaid.getText().equals("")) txtAmountPaid.setText("0.0");
            }
        });

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

    Transaction showDialog() {
        setData(this.transaction);
        setVisible(true);
        if (isModified(transaction)) {
            return transaction;
        }
        return null;
    }

    private void onOK() {
        // add your code here
        getData(this.transaction);
        this.transaction.setTransactionId(UUID.randomUUID().toString());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setData(Transaction data) {
        txtTransactionId.setText(data.getTransactionId());
        txtBuyerId.setText(data.getBuyerId());
        txtGameId.setText(data.getGameId());
        txtPublisherId.setText(data.getPublisherId());
        txtLicenseCode.setText(data.getLicenseCode());
        txtAmountPaid.setText(data.getAmountPaid().toString());
        dtpTransactionDate.setDateTimePermissive(data.getTransactionDate());
        txtSellerId.setText(data.getSellerId());
        txtSignature.setText(data.getSignature());
    }

    public void getData(Transaction data) {
        data.setTransactionId(txtTransactionId.getText());
        data.setBuyerId(txtBuyerId.getText());
        data.setGameId(txtGameId.getText());
        data.setPublisherId(txtPublisherId.getText());
        data.setLicenseCode(txtLicenseCode.getText());
        data.setAmountPaid(new BigDecimal(txtAmountPaid.getText()));
        data.setTransactionDate(dtpTransactionDate.getDateTimePermissive());
        data.setSellerId(txtSellerId.getText());
        data.setSignature(txtSignature.getText());
    }

    public boolean isModified(Transaction data) {
        if (txtTransactionId.getText() != null ?
                !txtTransactionId.getText().equals(data.getTransactionId()) :
                data.getTransactionId() != null) return true;
        if (txtBuyerId.getText() != null ? !txtBuyerId.getText().equals(data.getBuyerId()) : data.getBuyerId() != null)
            return true;
        if (txtGameId.getText() != null ? !txtGameId.getText().equals(data.getGameId()) : data.getGameId() != null)
            return true;
        if (txtPublisherId.getText() != null ?
                !txtPublisherId.getText().equals(data.getPublisherId()) :
                data.getPublisherId() != null) return true;
        if (txtLicenseCode.getText() != null ?
                !txtLicenseCode.getText().equals(data.getLicenseCode()) :
                data.getLicenseCode() != null) return true;
        if (txtAmountPaid.getText() != null ?
                !new BigDecimal(txtAmountPaid.getText()).equals(data.getAmountPaid()) :
                data.getAmountPaid() != null) return true;
        if (dtpTransactionDate.getDateTimePermissive() != null ?
                !dtpTransactionDate.getDateTimePermissive()
                                   .isEqual(data.getTransactionDate().truncatedTo(ChronoUnit.MINUTES)) :
                data.getTransactionDate() != null) return true;
        if (txtSellerId.getText() != null ?
                !txtSellerId.getText().equals(data.getSellerId()) :
                data.getSellerId() != null) return true;
        if (txtSignature.getText() != null ?
                !txtSignature.getText().equals(data.getSignature()) :
                data.getSignature() != null) return true;
        return false;
    }
}
