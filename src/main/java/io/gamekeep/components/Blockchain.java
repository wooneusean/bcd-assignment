package io.gamekeep.components;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Blockchain {
    private static LinkedList<Block> blockchain = new LinkedList<>(Arrays.asList(new Block("0")));
    private static String filePath;
    private static Block currentBlock;

    public static Block getCurrentBlock() {
        return currentBlock;
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
        currentBlock = blockchain.getLast();
        boolean isNewBlockCreated = false;
        if (currentBlock.getTransactions().size() >= Block.MAX_TRANSACTIONS) {
            currentBlock = new Block(currentBlock.getBlockHash());
            blockchain.add(currentBlock);
            isNewBlockCreated = true;
        }
        currentBlock.addTransaction(txn);
        return isNewBlockCreated;
    }

    public static void load() {
        try (FileInputStream f = new FileInputStream(filePath); ObjectInputStream o = new ObjectInputStream(f)) {
            blockchain = (LinkedList<Block>) o.readObject();
            System.out.println("Read from file");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void persist() {
        try (FileOutputStream f = new FileOutputStream(filePath); ObjectOutputStream o = new ObjectOutputStream(f)) {
            o.writeObject(blockchain);
            System.out.println("Saved to file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String distribute() {
        return blockchain.toString();
    }
}
