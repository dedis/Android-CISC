package com.epfl.dedis.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import net.i2p.crypto.eddsa.KeyPairGenerator;

public class Identity {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Identity() {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    ////////////////////////////////////////////
    // TODO actual key-storage and access
    ////////////////////////////////////////////
    public byte[] getPublicKey() {
        return publicKey.getEncoded();
    }

    public byte[] getPrivateKey() {
        return privateKey.getEncoded();
    }

    @Override
    public String toString() {
        return "Identity {\n" +
                "\tpublicKey=" + Arrays.toString(publicKey.getEncoded()) + ",\n" +
                "\tprivateKey=" + Arrays.toString(privateKey.getEncoded()) + "\n" +
                '}';
    }
}
