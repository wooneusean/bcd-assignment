package io.gamekeep.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricKeyPairGenerator {
    public static KeyPair generate() {
        try {
            String curveName = "secp256r1";
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
            kpg.initialize(new ECGenParameterSpec(curveName));
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
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

    public static KeyPair load(String path) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return new KeyPair(loadPublic(Paths.get(path, "public.key")), loadPrivate(Paths.get(path, "private.key")));
    }

    public static PrivateKey loadPrivate(Path path) throws
                                                    IOException,
                                                    NoSuchAlgorithmException,
                                                    InvalidKeySpecException {
        PKCS8EncodedKeySpec prvKey = new PKCS8EncodedKeySpec(Files.readAllBytes(path));
        return KeyFactory.getInstance("ECDSA").generatePrivate(prvKey);
    }

    public static PublicKey loadPublic(Path path) throws
                                                  NoSuchAlgorithmException,
                                                  InvalidKeySpecException,
                                                  IOException {
        X509EncodedKeySpec pubKey = new X509EncodedKeySpec(Files.readAllBytes(path));
        return KeyFactory.getInstance("ECDSA").generatePublic(pubKey);

    }
}
