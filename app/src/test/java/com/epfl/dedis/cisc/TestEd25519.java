package com.epfl.dedis.cisc;

import org.apache.commons.codec.binary.Base64;

import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.net.Identity;

import org.bouncycastle.jce.provider.symmetric.ARC4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricGradleTestRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;
/**
 * Created by Andrea on 25/08/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestEd25519 {
    @Test
    public void testStrings(){
        Ed25519 ed = new Ed25519();
        PublicKey pub = ed.getPublic();
        PrivateKey priv = ed.getPrivate();

        assertEquals(pub, Ed25519.BytesToPub(Ed25519.PubBytes(pub)));
        assertEquals(pub, Ed25519.StringToPub(Ed25519.PubString(pub)));
        assertEquals(priv, Ed25519.BytesToPrivate(Ed25519.PrivateBytes(priv)));
        assertEquals(priv, Ed25519.StringToPrivate(Ed25519.PrivateString(priv)));
    }
}
