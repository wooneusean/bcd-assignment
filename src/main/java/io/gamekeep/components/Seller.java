package io.gamekeep.components;

import io.gamekeep.crypto.AsymmetricKeyPairGenerator;
import io.gamekeep.crypto.Signer;

import java.security.KeyPair;

public class Seller {
    private final String sellerId;
    private KeyPair keyPair;

    public Seller(String sellerId) {
        this.sellerId = sellerId;
        loadKeys();
    }

    public Transaction digitallySign(Transaction txn) throws Exception {
        String signature = Signer.sign(txn.toString(), keyPair.getPrivate());
        txn.setSignature(signature);
        return txn;
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
