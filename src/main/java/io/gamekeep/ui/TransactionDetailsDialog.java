package io.gamekeep.ui;

import com.github.lgooddatepicker.components.DateTimePicker;
import io.gamekeep.components.Seller;
import io.gamekeep.components.Transaction;
import io.gamekeep.constants.GameKeepConstants;
import io.gamekeep.ui.documentfilters.NumberDocumentFilter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class TransactionDetailsDialog extends JDialog {

    private final Transaction transaction;
    private boolean isNewTransaction = true;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtBuyerId;
    private JTextField txtTransactionId;
    private JComboBox<Seller> cmbSigner;
    private JTextField txtAmountPaid;
    private JTextField txtLicenseCode;
    private JTextField txtPublisherId;
    private JTextField txtGameId;
    private DateTimePicker dtpTransactionDate;
    private JTextArea txtSignature;
    private JButton btnVerifySignature;
    private boolean isCancelled = false;

    public TransactionDetailsDialog(Transaction transaction, boolean isEditing) {
        this.transaction = transaction;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("New Transaction");
        pack();
        setLocationRelativeTo(null);

        cmbSigner.setModel(new DefaultComboBoxModel<>(GameKeepConstants.SELLERS.toArray(new Seller[0])));

        if (!isEditing) {
            isNewTransaction = false;
            setTitle("Transaction - " + transaction.getTransactionId());
            Arrays.stream(new JTextComponent[]{
                    txtBuyerId, txtTransactionId, txtAmountPaid, txtLicenseCode, txtPublisherId, txtGameId, txtSignature
            }).forEach(c -> {
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            c.setEditable(!c.isEditable());
                        }
                    }
                });

                c.setEditable(false);
            });
            cmbSigner.setEnabled(false);
            dtpTransactionDate.setEnabled(false);
            btnVerifySignature.setVisible(true);
        }

        ((PlainDocument) txtAmountPaid.getDocument()).setDocumentFilter(new NumberDocumentFilter());

        txtAmountPaid.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtAmountPaid.getText().equals("0"))
                    txtAmountPaid.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtAmountPaid.getText().equals(""))
                    txtAmountPaid.setText("0");
            }
        });

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        btnVerifySignature.addActionListener(e -> {
            Seller seller = ((Seller) cmbSigner.getSelectedItem());
            String message = "";
            try {
                if (this.transaction.verifySignature(seller.getKeyPair().getPublic())) {
                    message = "Transaction verification success.\n\nThe data has not been tampered with.";
                } else {
                    message = "Transaction verification failed.\n\nThe data has been tampered with or different signer has signed off this transaction.";
                }
            } catch (Exception ex) {
                message = "Transaction failed verification.\n\nThe signer has no key pairs available.";
            }
            JOptionPane.showMessageDialog(this, message, "Data Integrity Status", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    Transaction showDialog() {
        setData(transaction);
        setVisible(true);
        if (isCancelled)
            return null;

        try {
            if (isNewTransaction || isModified(transaction)) {
                getData(transaction);
                return transaction;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        isCancelled = true;
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
        txtSignature.setText(data.getSignature());
        try {
            cmbSigner.setSelectedItem(GameKeepConstants.SELLERS.stream()
                                                               .filter(s -> s.getSellerId().equals(data.getSellerId()))
                                                               .findFirst()
                                                               .get());
        } catch (Exception e) {
            cmbSigner.setSelectedIndex(-1);
        }
    }

    public void getData(Transaction data) {
        data.setTransactionId(txtTransactionId.getText());
        data.setBuyerId(txtBuyerId.getText());
        data.setGameId(txtGameId.getText());
        data.setPublisherId(txtPublisherId.getText());
        data.setLicenseCode(txtLicenseCode.getText());
        data.setAmountPaid(new BigDecimal(txtAmountPaid.getText()));
        data.setTransactionDate(dtpTransactionDate.getDateTimePermissive());
        data.setSellerId(((Seller) cmbSigner.getSelectedItem()).getSellerId());
    }

    public boolean isModified(Transaction data) {
        if (txtTransactionId.getText() != null ?
                !txtTransactionId.getText().equals(data.getTransactionId()) :
                data.getTransactionId() != null)
            return true;
        if (txtBuyerId.getText() != null ? !txtBuyerId.getText().equals(data.getBuyerId()) : data.getBuyerId() != null)
            return true;
        if (txtGameId.getText() != null ? !txtGameId.getText().equals(data.getGameId()) : data.getGameId() != null)
            return true;
        if (txtPublisherId.getText() != null ?
                !txtPublisherId.getText().equals(data.getPublisherId()) :
                data.getPublisherId() != null)
            return true;
        if (txtLicenseCode.getText() != null ?
                !txtLicenseCode.getText().equals(data.getLicenseCode()) :
                data.getLicenseCode() != null)
            return true;
        if (txtAmountPaid.getText() != null ?
                !new BigDecimal(txtAmountPaid.getText()).equals(data.getAmountPaid()) :
                data.getAmountPaid() != null)
            return true;
        if (dtpTransactionDate.getDateTimePermissive() != null ?
                !dtpTransactionDate.getDateTimePermissive()
                                   .isEqual(data.getTransactionDate().truncatedTo(ChronoUnit.MINUTES)) :
                data.getTransactionDate() != null)
            return true;
        try {
            String sellerId = ((Seller) cmbSigner.getSelectedItem()).getSellerId();
            if (sellerId != null ? !sellerId.equals(data.getSellerId()) : data.getSellerId() != null)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
