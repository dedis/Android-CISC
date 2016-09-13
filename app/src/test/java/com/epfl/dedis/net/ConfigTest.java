package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

// TODO: Implement hashing tests
@RunWith(JUnit4.class)
public class ConfigTest {

    /**
     * Two value equal configuration need to have an identical
     * hash string.
     *
     * @throws NoSuchAlgorithmException wrongly chosen algorithm
     */
    @Test
    public void twoIdependentConfigurationsResultInSameHash() throws NoSuchAlgorithmException {
        KeyPair keyPair = Ed25519.newKeyPair();
        Config config1 = new Config(3, "test1", keyPair.getPublic());
        Config config2 = new Config(3, "test1", keyPair.getPublic());

        assertArrayEquals(config1.hash(), config2.hash());

        config1.addData("test1", "public key");
        assertNotEquals(Arrays.toString(config1.hash()), Arrays.toString(config2.hash()));
        config2.addData("test1", "public key");
        assertEquals(Arrays.toString(config1.hash()), Arrays.toString(config2.hash()));
    }
}
