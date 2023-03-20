package io.gamekeep.components;

import io.gamekeep.crypto.AsymmetricKeyPairGenerator;
import io.gamekeep.crypto.Signer;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Objects;

public class User implements Serializable {
    private final String userId;
    private KeyPair keyPair;

    public User(String userId) {
        this.userId = userId;
        loadKeys();
    }

    @Override
    public String toString() {
        return userId;
    }

    public void digitallySign(Transaction txn) throws Exception {
        String signature = Signer.sign(txn.toString(), keyPair.getPrivate());
        txn.setSignature(signature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    private void loadKeys() {
        try {
            keyPair = AsymmetricKeyPairGenerator.load("keys/" + userId);
        } catch (Exception e) {
            keyPair = AsymmetricKeyPairGenerator.generate();
            AsymmetricKeyPairGenerator.save(keyPair, "keys/" + userId);
        }
    }

    public String getUserId() {
        return userId;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
