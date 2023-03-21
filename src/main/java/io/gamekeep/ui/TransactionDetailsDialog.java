package io.gamekeep.ui;

import io.gamekeep.components.Transaction;
import io.gamekeep.components.User;
import io.gamekeep.crypto.Encryptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class TransactionDetailsDialog extends JDialog {

    private final Transaction transaction;
    private boolean isNewTransaction = true;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtReceiverId;
    private JTextField txtTransactionId;
    private JTextField txtSenderId;
    private JTextField txtLicenseCode;
    private JTextField txtGameId;
    private JTextField txtTransactionDate;
    private JTextArea txtSignature;
    private JButton btnVerifySignature;
    private JButton btnDecrypt;
    private JPanel pnlTransactionDate;
    private JLabel lblSignature;
    private JPanel pnlSignature;
    private JScrollPane scrSignature;
    private JPanel pnlTransactionId;
    private boolean isCancelled = false;

    public TransactionDetailsDialog(Transaction transaction, boolean isEditing) {
        this.transaction = transaction;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("New Transaction");
        pack();
        setLocationRelativeTo(null);

        if (isEditing) {
            isNewTransaction = false;
            setTitle("Transaction - " + transaction.getTransactionId());
            Arrays.stream(new JTextComponent[]{
                    txtReceiverId,
                    txtTransactionDate,
                    txtSenderId,
                    txtTransactionId,
                    txtLicenseCode,
                    txtGameId,
                    txtSignature
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
            btnDecrypt.setVisible(true);
            btnVerifySignature.setVisible(true);
        } else {
            pnlTransactionId.setVisible(false);
            lblSignature.setVisible(false);
            scrSignature.setVisible(false);
            pnlTransactionDate.setVisible(false);
            btnDecrypt.setVisible(false);
            setPreferredSize(new Dimension(400, 200));
            pack();
        }

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
            User user = new User(txtSenderId.getText());
            String message;
            try {
                if (this.transaction.verifySignature(user.getKeyPair().getPublic())) {
                    message = "Transaction verification success.\n\nThe data has not been tampered with.";
                } else {
                    message = "Transaction verification failed.\n\nThe data has been tampered with or different signer has signed off this transaction.";
                }
            } catch (Exception ex) {
                message = "Transaction failed verification.\n\nThe signer has no key pairs available.";
            }
            JOptionPane.showMessageDialog(this, message, "Data Integrity Status", JOptionPane.INFORMATION_MESSAGE);
        });

        btnDecrypt.addActionListener(e -> {
            try {
                if (!User.keyFilesExists(transaction.getSenderId()) || !User.keyFilesExists(transaction.getReceiverId())) {
                   throw new RuntimeException("User key files does not exist.");
                }

                User sender = new User(transaction.getSenderId());
                User receiver = new User(transaction.getReceiverId());
                JOptionPane.showMessageDialog(this,
                                              "Decrypted License Code:\n\n" +
                                              Encryptor.decrypt(transaction.getLicenseCode(),
                                                                receiver.getKeyPair().getPrivate(),
                                                                sender.getKeyPair().getPublic()),
                                              "Decryption Success",
                                              JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                                              "Failed to decrypt license code.",
                                              "Decryption Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
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
        txtReceiverId.setText(data.getReceiverId());
        txtGameId.setText(data.getGameId());
        txtLicenseCode.setText(data.getLicenseCode());
        txtTransactionDate.setText(data.getTransactionDate());
        txtSignature.setText(data.getSignature());
        txtSenderId.setText(data.getSenderId());
    }

    public void getData(Transaction data) {
        data.setTransactionId(txtTransactionId.getText());
        data.setReceiverId(txtReceiverId.getText());
        data.setGameId(txtGameId.getText());
        data.setLicenseCode(txtLicenseCode.getText());
        data.setTransactionDate(txtTransactionDate.getText());
        data.setSenderId(txtSenderId.getText());
    }

    public boolean isModified(Transaction data) {
        if (txtTransactionId.getText() != null ?
                !txtTransactionId.getText().equals(data.getTransactionId()) :
                data.getTransactionId() != null)
            return true;
        if (txtReceiverId.getText() != null ?
                !txtReceiverId.getText().equals(data.getReceiverId()) :
                data.getReceiverId() != null)
            return true;
        if (txtGameId.getText() != null ? !txtGameId.getText().equals(data.getGameId()) : data.getGameId() != null)
            return true;
        if (txtLicenseCode.getText() != null ?
                !txtLicenseCode.getText().equals(data.getLicenseCode()) :
                data.getLicenseCode() != null)
            return true;
        if (txtTransactionDate.getText() != null ?
                !txtTransactionDate.getText().equals(data.getTransactionDate()) :
                data.getTransactionDate() != null)
            return true;
        return txtSenderId.getText() != null ?
                !txtSenderId.getText().equals(data.getSenderId()) :
                data.getSenderId() != null;
    }
}
