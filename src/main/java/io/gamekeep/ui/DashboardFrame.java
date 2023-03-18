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
import java.util.ListIterator;

public class DashboardFrame extends JFrame {
    private final TransactionTableModel mdlTransactions = new TransactionTableModel();
    private ListIterator<Block> blockListIterator = Blockchain.getBlockchain().listIterator(0);
    private Block currentBlock = Blockchain.getCurrentBlock();
    private JPanel contentPane;
    private JButton btnPrevious;
    private JButton btnNext;
    private JButton btnAddTransaction;
    private JTextField txtPreviousBlockHash;
    private JList<Transaction> lstTransactions;
    private JTextField txtTimestamp;
    private JTextField txtMerkleRoot;
    private JTextField txtCurrentBlockHash;
    private JTable tblTransactions;

    public DashboardFrame(String title) {
        super(title);
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        refresh();

        tblTransactions.setModel(mdlTransactions);
        tblTransactions.setDefaultRenderer(BigDecimal.class, new CurrencyTableCellRenderer());
        tblTransactions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblTransactions.rowAtPoint(e.getPoint());
                JTable tbl = (JTable) e.getSource();
                TransactionTableModel mdl = (TransactionTableModel) tbl.getModel();
                if (row >= 0 && e.getClickCount() >= 2) {
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
                            "Current block full!",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    goToNextBlock();
                    return;
                }
                mdlTransactions.fireTableDataChanged();
            }
        });

        btnPrevious.addActionListener(e -> goToPrevBlock());
        btnNext.addActionListener(e -> goToNextBlock());
    }

    private void goToPrevBlock() {
        if (blockListIterator.hasPrevious()) {
            System.out.println(blockListIterator.previousIndex());
            currentBlock = blockListIterator.previous();
        }
        refresh();
    }

    private void goToNextBlock() {
        if (blockListIterator.hasNext()) {
            System.out.println(blockListIterator.nextIndex());
            currentBlock = blockListIterator.next();
        }
        refresh();
    }

    private void refresh() {
        txtMerkleRoot.setText(currentBlock.getMerkleRoot());
        txtTimestamp.setText(String.valueOf(currentBlock.getTimestamp()));
        txtCurrentBlockHash.setText(currentBlock.getBlockHash());
        txtPreviousBlockHash.setText(currentBlock.getPreviousBlockHash());
        btnNext.setEnabled(blockListIterator.hasNext());
        btnPrevious.setEnabled(blockListIterator.hasPrevious());
        mdlTransactions.setTransactionList(currentBlock.getTransactions());
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
