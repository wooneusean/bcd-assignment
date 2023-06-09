package io.gamekeep.components;

import io.gamekeep.constants.AppConstants;
import io.gamekeep.crypto.Hasher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block implements Serializable {
    public static final int MAX_TRANSACTIONS = 10;
    private final List<Transaction> transactions = new ArrayList<>();
    private final String previousBlockHash;
    private final long timestamp;
    private final long blockNumber;

    public Block(String previousBlockHash) {
        this.blockNumber = Blockchain.getBlockchain().size() + 1;
        this.previousBlockHash = previousBlockHash;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean addTransaction(Transaction txn) {
        if (transactions.size() >= MAX_TRANSACTIONS)
            throw new RuntimeException("Maximum number of transactions for this block reached. (MAX_TRANSACTIONS = " +
                                       MAX_TRANSACTIONS +
                                       ")");
        return transactions.add(txn);
    }

    @Override
    public String toString() {
        return "{\"Block\":{" +
               "\"transactions\":" + transactions +
               ", \"previousBlockHash\":\"" + previousBlockHash + '\"' +
               ", \"timestamp\":" + timestamp +
               "}}";
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getBlockHash() {
        return Hasher.hash(previousBlockHash, String.valueOf(timestamp), getMerkleRoot(), AppConstants.GAME_KEEP_SALT);
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getMerkleRoot() {
        if (transactions.size() == 0) return "";

        List<String> merkleNodes = transactions.stream()
                                               .map(txn -> Hasher.hash(txn.toString(), AppConstants.GAME_KEEP_SALT))
                                               .collect(Collectors.toList());

        while (merkleNodes.size() > 1) {
            String left = merkleNodes.remove(0);
            String right = merkleNodes.remove(0);
            merkleNodes.add(Hasher.hash(left, right, AppConstants.GAME_KEEP_SALT));
        }
        return merkleNodes.get(0);
    }

    public boolean isFull() {
        return transactions.size() >= MAX_TRANSACTIONS;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getBlockNumber() {
        return blockNumber;
    }
}
