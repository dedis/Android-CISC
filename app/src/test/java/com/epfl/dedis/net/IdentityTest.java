package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class IdentityTest {

    /**
     * GSON does not change any field value after applying its
     * serialization.
     */
    @Test
    public void unchangedFieldsAfterSerialization() {
        Identity identity = new Identity("test", null);
        Identity copy = Utils.fromJson(Utils.toJson(identity), Identity.class);

        assertEquals(identity.getPublic(), copy.getPublic());
        assertEquals(identity.getPrivate(), copy.getPrivate());
        assertEquals(identity.getName(), copy.getName());
    }

    /**
     * A newly added device does appear in the proposed configuration
     * but not in latest one.
     */
    @Test
    public void correctlyAddNewDevice() {
        Identity identity = new Identity(null, new byte[]{});
        identity.setConfig(new Config(3, new HashMap<String, String>(), new HashMap<String, String>()));
        identity.newDevice("test");

        assertNotNull(identity.getName());
        assertNotNull(identity.getProposed());

        assertEquals(1, identity.getProposed().getDevice().size());
        assertEquals(0, identity.getProposed().getData().size());
    }

    /**
     * When updating the owner's data the proposed configuration is
     * set.
     */
    @Test
    public void correctlyUpdateData() {
        Identity identity = new Identity("test", null);

        assertNotNull(identity.getName());
        assertNotNull(identity.getSeed());

        assertEquals(1, identity.getConfig().getDevice().size());
        assertNull(identity.getProposed());

        identity.updateData("dedis");

        assertEquals(1, identity.getProposed().getData().size());
        assertEquals("dedis", identity.getProposed().getData().get("test"));
    }
}
