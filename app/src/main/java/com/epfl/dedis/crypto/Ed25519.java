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

    public byte[] getPublic() {
        return pub.getEncoded();
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
