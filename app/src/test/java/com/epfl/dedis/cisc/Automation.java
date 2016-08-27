package com.epfl.dedis.cisc;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.CreateIdentity;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Identity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class Automation {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";
    private static final String DEVICE = "MOTOROLA";

    private static Identity identity;
    private static Activity activity;

    @BeforeClass
    public static void setUpBeforeClass() {
        activity = new Activity() {
            public void callbackSuccess() {}
            public void callbackError(int error) {}
        };

        Cothority cot = new Cothority(HOST, PORT);
        CreateIdentity id = new CreateIdentity(activity, cot, true);
        identity = id.getIdentity();
    }

    @Test
    public void addIdentity() {
        assertEquals(DEVICE, identity.getDeviceName());
        assertEquals(identity.getPub(), identity.getConfig().getDevice().get(DEVICE));
    }

    @Test
    public void configUpdateExistingIdentity() {
        ConfigUpdate cu = new ConfigUpdate(activity, identity, true);
        Identity id2 = cu.getIdentity();
        assertEquals(identity.getPub(), id2.getPub());
        assertEquals(identity.getSkipchainId(), id2.getSkipchainId());
    }

    @Test
    public void mconfigUpdateInexistentIdentity() {
        Identity mock = new Identity(DEVICE, identity.getCothority());
        mock.setSkipchainId(new byte[]{1, 2, 3});
        ConfigUpdate cu = new ConfigUpdate(activity, mock, true);
        assertNotEquals(identity, cu.getIdentity());
    }
}