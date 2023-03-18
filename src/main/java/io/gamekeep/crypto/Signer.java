package io.gamekeep.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class Signer {
    public static String sign(String data, PrivateKey key) throws Exception {
        Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return Base64.getEncoder().encodeToString(sig.sign());
    }

    public static boolean verify(String data, String signature, PublicKey key) throws Exception {
        Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initVerify(key);
        sig.update(data.getBytes());
        return sig.verify(Base64.getDecoder().decode(signature));
    }
}
