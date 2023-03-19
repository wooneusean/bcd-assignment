package io.gamekeep.components;

import java.io.*;
import java.util.LinkedList;

public class Blockchain {
    private static LinkedList<Block> blockchain = new LinkedList<>();
    private static String filePath;

    public static Block getCurrentBlock() {
        return blockchain.getLast();
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        Blockchain.filePath = filePath;
    }

    public static LinkedList<Block> getBlockchain() {
        return blockchain;
    }

    public static boolean pushTransaction(Transaction txn) {
        Block currentBlock;
        try {
            currentBlock = blockchain.getLast();
        } catch (Exception e) {
            currentBlock = new Block("0");
            blockchain.add(currentBlock);
        }


        boolean isNewBlockCreated = false;
        if (currentBlock.getTransactions().size() >= Block.MAX_TRANSACTIONS) {
            currentBlock = new Block(currentBlock.getBlockHash());
            blockchain.add(currentBlock);
            isNewBlockCreated = true;
        }
        currentBlock.addTransaction(txn);
        persist();
        return isNewBlockCreated;
    }


    @SuppressWarnings("unchecked")
    public static boolean load() {
        try (FileInputStream fs = new FileInputStream(filePath); ObjectInputStream os = new ObjectInputStream(fs)) {
            blockchain = (LinkedList<Block>) os.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Blockchain.load() > No file to load.");
            return false;
        }
    }

    public static boolean persist() {
        File storageFile = new File(filePath);
        storageFile.getParentFile().mkdirs();
        try {
            storageFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fs = new FileOutputStream(storageFile); ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(blockchain);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String distribute() {
        return blockchain.toString();
    }
}
