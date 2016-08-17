package com.epfl.dedis.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import net.i2p.crypto.eddsa.KeyPairGenerator;

public class Ed25519 {

    private PublicKey pub;
    private PrivateKey secret;

    public Ed25519() {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        this.pub = keyPair.getPublic();
        this.secret = keyPair.getPrivate();
    }

    public int[] getPublic() {
        byte[] encoded = pub.getEncoded();
        int[] conv = new int[encoded.length];
        for (int i = 0; i < conv.length; i++) {
            if (encoded[i] < 0) {
                conv[i] = encoded[i] + 256;
            } else {
                conv[i] = encoded[i];
            }
        }
        return conv;
    }

    public byte[] getPrivate() {
        return secret.getEncoded();
    }

    @Override
    public String toString() {
        return "Ed25519 {\n" +
                "\tpublicKey=" + Arrays.toString(pub.getEncoded()) + ",\n" +
                "\tprivateKey=" + Arrays.toString(secret.getEncoded()) + "\n" +
                '}';
    }
}
