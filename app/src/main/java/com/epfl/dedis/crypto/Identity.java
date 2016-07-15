package com.epfl.dedis.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import net.i2p.crypto.eddsa.KeyPairGenerator;

/**
 * The Identity class generates and contains the public and private
 * Ed25519-keys of a device. Used for Cothority authentication.
 *
 * @author Ignacio Aléman
 * @author Andrea Caforio
 * @since 14.07.2016
 */
public class Identity {

    /* Ed25519 key pair */
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

    /**
     * Print neatly.
     *
     * @return identity string
     */
    @Override
    public String toString() {
        return "Identity {\n" +
                "\tpublicKey=" + Arrays.toString(publicKey.getEncoded()) + ",\n" +
                "\tprivateKey=" + Arrays.toString(privateKey.getEncoded()) + "\n" +
                '}';
    }
}
