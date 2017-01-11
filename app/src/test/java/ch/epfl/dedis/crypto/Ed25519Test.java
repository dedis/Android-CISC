package ch.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class Ed25519Test {

    @Test
    public void performCorrectKeyPairConversions(){
        KeyPair keyPair = Ed25519.newKeyPair();
        PublicKey pub = keyPair.getPublic();

        assertEquals(pub, Ed25519.BytesToPub(Ed25519.PubBytes(pub)));
        assertEquals(pub, Ed25519.StringToPub(Ed25519.PubString(pub)));
    }
}