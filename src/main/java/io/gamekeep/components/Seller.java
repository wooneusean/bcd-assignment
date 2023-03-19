package io.gamekeep.components;

import io.gamekeep.crypto.AsymmetricKeyPairGenerator;
import io.gamekeep.crypto.Signer;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Objects;

public class Seller implements Serializable {
    private final String sellerId;
    private KeyPair keyPair;

    public Seller(String sellerId) {
        this.sellerId = sellerId;
        loadKeys();
    }

    @Override
    public String toString() {
        return sellerId;
    }

    public Transaction digitallySign(Transaction txn) throws Exception {
        String signature = Signer.sign(txn.toString(), keyPair.getPrivate());
        txn.setSignature(signature);
        return txn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return sellerId.equals(seller.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId);
    }

    private void loadKeys() {
        try {
            keyPair = AsymmetricKeyPairGenerator.load("keys/" + sellerId);
        } catch (Exception e) {
            keyPair = AsymmetricKeyPairGenerator.generate();
            AsymmetricKeyPairGenerator.save(keyPair, "keys/" + sellerId);
        }
    }

    public String getSellerId() {
        return sellerId;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
