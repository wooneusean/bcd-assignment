package io.gamekeep.crypto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricKeyPairGenerator {
    public static KeyPair generate() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(KeyPair kp, String path) {
        File f = new File(path);
        f.mkdirs();
        try {
            Files.write(Paths.get(path, "private.key"), kp.getPrivate().getEncoded(), StandardOpenOption.CREATE);
            Files.write(Paths.get(path, "public.key"), kp.getPublic().getEncoded(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair load(String path) {
        return new KeyPair(
                loadPublic(Paths.get(path, "public.key")),
                loadPrivate(Paths.get(path, "private.key"))
        );
    }

    public static PrivateKey loadPrivate(Path path) {
        try {
            PKCS8EncodedKeySpec prvKey = new PKCS8EncodedKeySpec(Files.readAllBytes(path));
            return KeyFactory.getInstance("RSA").generatePrivate(prvKey);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey loadPublic(Path path) {
        try {
            X509EncodedKeySpec pubKey = new X509EncodedKeySpec(Files.readAllBytes(path));
            return KeyFactory.getInstance("RSA").generatePublic(pubKey);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
