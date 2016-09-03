package com.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class Ed25519Test {

    /**
     * Testing correct public key manipulations.
     */
    @Test
    public void keyPairIsCorrectlyConvertedToString(){
        KeyPair keyPair = Ed25519.newKeyPair();
        PublicKey pub = keyPair.getPublic();
        PrivateKey sec = keyPair.getPrivate();

        assertEquals(pub, Ed25519.BytesToPub(Ed25519.PubBytes(pub)));
        assertEquals(pub, Ed25519.StringToPub(Ed25519.PubString(pub)));
        assertEquals(sec, Ed25519.BytesToPrivate(Ed25519.PrivateBytes(sec)));
        assertEquals(sec, Ed25519.StringToPrivate(Ed25519.PrivateString(sec)));
    }
}
