package com.epfl.dedis.cisc;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.CreateIdentity;
import com.epfl.dedis.api.ProposeSend;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Identity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class Automation {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";
    private static final String DEVICE = "MOTOROLA";
    private static final byte[] FOO = new byte[]{1, 2, 3};

    private static Identity identity;
    private static Activity activity;

    @BeforeClass
    public static void setUpBeforeClass() {
        activity = new Activity() {
            public void callbackSuccess() {}
            public void callbackError(int error) {}
        };

        Cothority cothority = new Cothority(HOST, PORT);
        CreateIdentity ci = new CreateIdentity(activity, cothority, true);
        identity = ci.getIdentity();
    }

    @Test
    public void addIdentityEmptySkipchain() {
        assertEquals(DEVICE, identity.getDeviceName());
        assertEquals(identity.getPub(), identity.getConfig().getDevice().get(DEVICE));
    }

    @Test
    public void configUpdateExistingIdentity() {
        int priorThreshold = identity.getConfig().getThreshold();
        String priorPublicKey = identity.getConfig().getDeviceB64().get(DEVICE);

        ConfigUpdate cu = new ConfigUpdate(activity, identity, true);
        Config config = cu.getConfig();

        assertEquals(priorThreshold, config.getThreshold());
        assertEquals(priorPublicKey, config.getDeviceB64().get(DEVICE));
    }

    @Test
    public void configUpdateInexistentIdentity() {
        Identity mockIdentity = new Identity(DEVICE, identity.getCothority(), FOO);
        ConfigUpdate cu = new ConfigUpdate(activity, mockIdentity, true);
        assertNull(cu.getConfig());
    }

    @Test
    public void proposeSendExistingIdentity() {
        identity.newDevice();
        String priorPublicKey = identity.getProposed().getDeviceB64().get(DEVICE);

        ProposeSend proposeSend = new ProposeSend(activity, identity, true);
        Config proposed = proposeSend.getProposed();

        assertEquals(priorPublicKey, proposed.getDeviceB64().get(DEVICE));
    }
}