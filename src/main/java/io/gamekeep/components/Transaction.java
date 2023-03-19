package io.gamekeep.components;

import io.gamekeep.crypto.Signer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private String transactionId;
    private String buyerId;
    private String gameId;
    private String publisherId;
    private String licenseCode;
    private BigDecimal amountPaid;
    private LocalDateTime transactionDate;
    private String sellerId;
    private String signature;

    public Transaction(
            String transactionId,
            String buyerId,
            String gameId,
            String publisherId,
            String licenseCode,
            BigDecimal amountPaid,
            LocalDateTime transactionDate,
            String sellerId
    ) {
        this.transactionId = transactionId;
        this.buyerId = buyerId;
        this.gameId = gameId;
        this.publisherId = publisherId;
        this.licenseCode = licenseCode;
        this.amountPaid = amountPaid;
        this.transactionDate = transactionDate;
        this.sellerId = sellerId;
        this.signature = "";
    }

    @Override
    public String toString() {
        return "{\"Transaction\":{" + "\"transactionId\":\"" + transactionId + '\"' + ", \"buyerId\":\"" + buyerId +
               '\"' + ", \"gameId\":\"" + gameId + '\"' + ", \"publisherId\":\"" + publisherId + '\"' +
               ", \"licenseCode\":\"" + licenseCode + '\"' + ", \"amountPaid\":" + amountPaid +
               ", \"transactionDate\":" + transactionDate + ", \"sellerId\":\"" + sellerId + '\"' + "}}";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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
