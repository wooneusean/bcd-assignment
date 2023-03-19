package io.gamekeep.components;

import io.gamekeep.crypto.Hasher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block implements Serializable {
    public static final int MAX_TRANSACTIONS = 2;
    private final List<Transaction> transactions = new ArrayList<>();
    private final String previousBlockHash;
    private final long timestamp;

    public Block(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean addTransaction(Transaction txn) {
        if (transactions.size() >= MAX_TRANSACTIONS) throw new RuntimeException(
                "Maximum number of transactions for this block reached. (MAX_TRANSACTIONS = " + MAX_TRANSACTIONS + ")");
        return transactions.add(txn);
    }

    @Override
    public String toString() {
        return "{\"Block\":{" + "\"transactions\":" + transactions + ", \"previousBlockHash\":\"" + previousBlockHash +
               '\"' + ", \"timestamp\":" + timestamp + "}}";
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getBlockHash() {
        return Hasher.hash(previousBlockHash, String.valueOf(timestamp), getMerkleRoot());
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getMerkleRoot() {
        List<String> merkleNodes = transactions.stream()
                                               .map(txn -> Hasher.hash(txn.toString()))
                                               .collect(Collectors.toList());

        while (merkleNodes.size() > 1) {
            String left = merkleNodes.remove(0);
            String right = merkleNodes.remove(0);
            merkleNodes.add(Hasher.hash(left, right));
        }
        return merkleNodes.get(0);
    }

    public boolean isFull() {
        return transactions.size() >= MAX_TRANSACTIONS;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
