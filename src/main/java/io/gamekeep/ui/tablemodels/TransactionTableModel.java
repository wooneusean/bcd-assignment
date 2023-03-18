package io.gamekeep.ui.tablemodels;

import io.gamekeep.components.Transaction;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {
    private List<Transaction> transactionList;

    public TransactionTableModel() {
        transactionList = new ArrayList<>();
    }

    public TransactionTableModel(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return transactionList.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Transaction ID";
            case 1:
                return "Buyer ID";
            case 2:
                return "Seller ID";
            case 3:
                return "Amount Paid";
            case 4:
                return "Transaction Date";
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
                return String.class;
            case 3:
                return BigDecimal.class;
            case 4:
                return LocalDateTime.class;
        }
        return Object.class;
    }

    public Transaction get(int rowIndex) {
        return transactionList.get(rowIndex);
    }

    public void setRowValue(int rowIndex, Transaction newTxn) {
        Transaction txn = transactionList.get(rowIndex);
        txn.setTransactionId(newTxn.getTransactionId());
        txn.setBuyerId(newTxn.getBuyerId());
        txn.setGameId(newTxn.getGameId());
        txn.setPublisherId(newTxn.getPublisherId());
        txn.setLicenseCode(newTxn.getLicenseCode());
        txn.setAmountPaid(newTxn.getAmountPaid());
        txn.setTransactionDate(newTxn.getTransactionDate());
        txn.setSellerId(newTxn.getSellerId());
        txn.setSignature(newTxn.getSignature());
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction txn = transactionList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return txn.getTransactionId();
            case 1:
                return txn.getBuyerId();
            case 2:
                return txn.getSellerId();
            case 3:
                return txn.getAmountPaid();
            case 4:
                return txn.getTransactionDate();
        }
        return null;
    }

    public void add(Transaction txn) {
        transactionList.add(txn);
        int row = transactionList.indexOf(txn);
        fireTableRowsInserted(row, row);
    }

    public void remove(Transaction txn) {
        if (transactionList.contains(txn)) {
            int row = transactionList.indexOf(txn);
            transactionList.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
