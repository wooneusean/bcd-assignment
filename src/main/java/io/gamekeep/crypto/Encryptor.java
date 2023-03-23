package io.gamekeep.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IEKeySpec;
import org.bouncycastle.jce.spec.IESParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

public class Encryptor {
    static final byte[] d = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
    static final byte[] e = new byte[]{8, 7, 6, 5, 4, 3, 2, 1};
    static final IESParameterSpec param = new IESParameterSpec(d, e, 256);

    public static String decrypt(
            String ciphertext,
            PrivateKey receiverPrivateKey,
            PublicKey senderPublicKey
    ) throws
      InvalidKeyException,
      IllegalBlockSizeException,
      BadPaddingException,
      NoSuchPaddingException,
      NoSuchAlgorithmException,
      NoSuchProviderException,
      InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);

        cipher.init(Cipher.DECRYPT_MODE, new IEKeySpec(receiverPrivateKey, senderPublicKey), param);
        byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));

        return new String(plainBytes);
    }

    public static String encrypt(
            String plaintext,
            PrivateKey senderPrivateKey,
            PublicKey receiverPublicKey
    ) throws
      InvalidKeyException,
      IllegalBlockSizeException,
      BadPaddingException,
      NoSuchPaddingException,
      NoSuchAlgorithmException,
      NoSuchProviderException,
      InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        String ciphertext;

        cipher.init(Cipher.ENCRYPT_MODE, new IEKeySpec(senderPrivateKey, receiverPublicKey), param);
        byte[] cipherBytes = cipher.doFinal(plaintext.getBytes());
        ciphertext = Base64.getEncoder().encodeToString(cipherBytes);

        return ciphertext;
    }
}
