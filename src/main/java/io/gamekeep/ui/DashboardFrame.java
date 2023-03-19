package io.gamekeep.ui;

import io.gamekeep.components.Block;
import io.gamekeep.components.Blockchain;
import io.gamekeep.components.Transaction;
import io.gamekeep.ui.tablecellrenderers.CurrencyTableCellRenderer;
import io.gamekeep.ui.tablemodels.TransactionTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
                    TransactionDetailsDialog transactionDetailsDialog = new TransactionDetailsDialog(mdl.get(row));
                    transactionDetailsDialog.showDialog();
                }
            }
        });

        btnAddTransaction.addActionListener(e -> {
            Transaction transaction = new Transaction("", "", "", "", "", new BigDecimal("0"), LocalDateTime.now(), "");
            TransactionDetailsDialog txnDialog = new TransactionDetailsDialog(transaction);
            transaction = txnDialog.showDialog();
            if (transaction != null) {
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
                txtCurrentBlockHash.setText(currentBlock.getBlockHash());
                txtMerkleRoot.setText(currentBlock.getMerkleRoot());
                mdlTransactions.fireTableDataChanged();
            }
        });

        btnPrevious.addActionListener(e -> navigateBlock(-1));
        btnNext.addActionListener(e -> navigateBlock(1));
    }

    private void navigateBlock(int difference) {
        if (currentBlockIndex + difference > Blockchain.getBlockchain().size() - 1 ||
            currentBlockIndex + difference < 0) return;

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
