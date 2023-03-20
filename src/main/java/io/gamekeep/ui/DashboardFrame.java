package io.gamekeep.ui;

import io.gamekeep.components.Block;
import io.gamekeep.components.Blockchain;
import io.gamekeep.components.Transaction;
import io.gamekeep.components.User;
import io.gamekeep.crypto.Encryptor;
import io.gamekeep.ui.tablecellrenderers.CurrencyTableCellRenderer;
import io.gamekeep.ui.tablemodels.TransactionTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DashboardFrame extends JFrame {
    private final TransactionTableModel mdlTransactions = new TransactionTableModel();
    private Block currentBlock = Blockchain.getCurrentBlock();
    private JPanel contentPane;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnAddTransaction;
    private JTextArea txtPreviousBlockHash;
    private JTextField txtTimestamp;
    private JTextArea txtMerkleRoot;
    private JTextArea txtCurrentBlockHash;
    private JTable tblTransactions;
    private JButton validateBlockchainButton;
    private int currentBlockIndex = Blockchain.getBlockchain().size() - 1;

    public DashboardFrame(String title) {
        super(title);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        refresh();

        tblTransactions.setModel(mdlTransactions);
        tblTransactions.setDefaultRenderer(BigDecimal.class, new CurrencyTableCellRenderer());
        tblTransactions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblTransactions.rowAtPoint(e.getPoint());
                JTable tbl = (JTable) e.getSource();
                TransactionTableModel mdl = (TransactionTableModel) tbl.getModel();
                if (row >= 0 && e.getClickCount() == 2) {
                    TransactionDetailsDialog transactionDetailsDialog = new TransactionDetailsDialog(
                            mdl.get(row),
                            true
                    );
                    Transaction transaction = transactionDetailsDialog.showDialog();
                    if (transaction != null) {
                        Blockchain.persist();

                        refresh();
                    }
                }
            }
        });

        btnAddTransaction.addActionListener(e -> {
            Transaction transaction = new Transaction("", "", "", "", LocalDateTime.now().toString(), "");
            TransactionDetailsDialog txnDialog = new TransactionDetailsDialog(transaction, false);
            transaction = txnDialog.showDialog();
            if (transaction != null && transaction.getTransactionId().equals("")) {
                transaction.setTransactionId(UUID.randomUUID().toString());

                try {
                    User receiver = new User(transaction.getReceiverId());
                    transaction.setLicenseCode(Encryptor.encrypt(transaction.getLicenseCode(), receiver.getKeyPair().getPublic()));
                    User sender = new User(transaction.getSenderId());
                    sender.digitallySign(transaction);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to create new transaction. Signer or Signer KeyPair does not exist.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                boolean isNewBlockCreated = Blockchain.pushTransaction(transaction);
                if (isNewBlockCreated) {
                    JOptionPane.showMessageDialog(
                            this,
                            "The current block is full, creating new block.",
                            "Attention",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    navigateBlock(1);
                    return;
                }

                refresh();
            }
        });

        btnPrevious.addActionListener(e -> navigateBlock(-1));
        btnNext.addActionListener(e -> navigateBlock(1));

        validateBlockchainButton.addActionListener(e -> {
            for (int i = Blockchain.getBlockchain().size() - 1; i >= 1; i--) {
                Block currentBlock = Blockchain.getBlockchain().get(i);
                Block previousBlock = Blockchain.getBlockchain().get(i - 1);

                if (!currentBlock.getPreviousBlockHash().equals(previousBlock.getBlockHash())) {
                    JOptionPane.showMessageDialog(
                            this,
                            String.format(
                                    "This blockchain is invalid, block [%1.16s] predecessor [%1.16s] does not exist.",
                                    currentBlock.getBlockHash(),
                                    currentBlock.getPreviousBlockHash()
                            ),
                            "Invalid Blockchain",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "This blockchain is valid. All blocks have existing predecessors.",
                    "Valid Blockchain",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void navigateBlock(int difference) {
        if (currentBlockIndex + difference > Blockchain.getBlockchain().size() - 1 ||
                currentBlockIndex + difference < 0)
            return;

        currentBlockIndex += difference;
        currentBlock = Blockchain.getBlockchain().get(currentBlockIndex);
        refresh();
    }

    private void refresh() {
        txtMerkleRoot.setText(currentBlock.getMerkleRoot());
        txtTimestamp.setText(String.valueOf(currentBlock.getTimestamp()));
        txtCurrentBlockHash.setText(currentBlock.getBlockHash());
        txtPreviousBlockHash.setText(currentBlock.getPreviousBlockHash());
        btnNext.setEnabled(currentBlockIndex < Blockchain.getBlockchain().size() - 1);
        btnPrevious.setEnabled(currentBlockIndex > 0);
        mdlTransactions.setTransactionList(currentBlock.getTransactions());
    }
}
