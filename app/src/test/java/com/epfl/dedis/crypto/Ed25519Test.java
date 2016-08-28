package com.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class Ed25519Test {

    @Test
    public void keyPairIsCorrectlyConvertedToString(){
        Ed25519 ed = new Ed25519();
        PublicKey pub = ed.getPublic();
        PrivateKey priv = ed.getPrivate();

        assertEquals(pub, Ed25519.BytesToPub(Ed25519.PubBytes(pub)));
        assertEquals(pub, Ed25519.StringToPub(Ed25519.PubString(pub)));
        assertEquals(priv, Ed25519.BytesToPrivate(Ed25519.PrivateBytes(priv)));
        assertEquals(priv, Ed25519.StringToPrivate(Ed25519.PrivateString(priv)));
    }
}
