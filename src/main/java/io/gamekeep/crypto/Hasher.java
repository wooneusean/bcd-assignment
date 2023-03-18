package io.gamekeep.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hasher {
    public static String hash(String... dataList) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        for (String data : dataList) {
            md.update(data.getBytes());
        }
        return Base64.getEncoder().encodeToString(md.digest());
    }
}
