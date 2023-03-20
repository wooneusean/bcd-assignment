package io.gamekeep.ui.tablemodels;

import io.gamekeep.components.Transaction;

import javax.swing.table.AbstractTableModel;
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
                return "Sender ID";
            case 2:
                return "Receiver ID";
            case 3:
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
        txn.setReceiverId(newTxn.getReceiverId());
        txn.setGameId(newTxn.getGameId());
        txn.setLicenseCode(newTxn.getLicenseCode());
        txn.setTransactionDate(newTxn.getTransactionDate());
        txn.setSenderId(newTxn.getSenderId());
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
                return txn.getSenderId();
            case 2:
                return txn.getReceiverId();
            case 3:
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
