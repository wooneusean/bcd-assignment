package io.gamekeep.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Encryptor {
    public static String decrypt(String ciphertext, PrivateKey key) throws
                                                                    InvalidKeyException,
                                                                    IllegalBlockSizeException,
                                                                    BadPaddingException,
                                                                    NoSuchPaddingException,
                                                                    NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));

        return new String(plainBytes);
    }

    public static String encrypt(String plaintext, PublicKey key) throws
                                                                  InvalidKeyException,
                                                                  IllegalBlockSizeException,
                                                                  BadPaddingException,
                                                                  NoSuchPaddingException,
                                                                  NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA");
        String ciphertext;

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherBytes = cipher.doFinal(plaintext.getBytes());
        ciphertext = Base64.getEncoder().encodeToString(cipherBytes);

        return ciphertext;
    }
}
