package io.gamekeep.components;

import io.gamekeep.crypto.Signer;

import java.io.Serializable;
import java.security.PublicKey;

public class Transaction implements Serializable {
    private String transactionId;
    private String senderId;
    private String receiverId;
    private String gameId;
    private String licenseCode;
    private String transactionDate;
    private String signature;

    public Transaction(
            String transactionId,
            String receiverId,
            String gameId,
            String licenseCode,
            String transactionDate,
            String senderId
    ) {
        this.transactionId = transactionId;
        this.receiverId = receiverId;
        this.gameId = gameId;
        this.licenseCode = licenseCode;
        this.transactionDate = transactionDate;
        this.senderId = senderId;
        this.signature = "";
    }

    @Override
    public String toString() {
        return "{\"Transaction\":{" +
                "\"transactionId\":\"" + transactionId + '\"' +
                ", \"buyerId\":\"" + receiverId + '\"' +
                ", \"gameId\":\"" + gameId + '\"' +
                ", \"licenseCode\":\"" + licenseCode + '\"' +
                ", \"transactionDate\":" + transactionDate +
                ", \"sellerId\":\"" + senderId + '\"' +
                "}}";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean verifySignature(PublicKey pubKey) throws Exception {
        return Signer.verify(this.toString(), signature, pubKey);
    }
}
