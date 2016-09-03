package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ProposeSendTest extends APITest {

    /**
     * A new device is added to an existing Skipchain. For testing purposes
     * the Cothority returns the proposed configuration for a successful
     * ProposeSend request.
     */
    @Test
    public void cothorityReturnsValidProposedForNewDevice() {
        // Adding a device to a configuration and storing the posterior data
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.newDevice(NAME2);
        int priorThreshold = identity.getProposed().getThreshold();
        String priorPublicKey = identity.getProposed().getDevice().get(NAME2);
        Config proposed = new ProposeSend(activity, identity, true).getProposed();

        assertEquals(priorThreshold, proposed.getThreshold());
        assertEquals(priorPublicKey, proposed.getDevice().get(NAME2));
        assertEquals(2, proposed.getDevice().size());
    }

    /**
     * A device updates its data then sends ProposeSend request.
     * Prior and posterior data have to be equal.
     */
    @Test
    public void cothorityReturnsValidProposedForDataUpdate() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.updateData("test data");
        String priorData = identity.getProposed().getData().get(NAME1);
        Config proposed = new ProposeSend(activity, identity, true).getProposed();

        assertEquals(priorData, proposed.getData().get(NAME1));
        assertEquals(1, proposed.getData().size());
    }


    /**
     * Correct error message is returned when a ProposeSend request
     * contains an invalid network address.
     */
    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        Identity identity = new Identity(NAME1, cothority("foo", PORT));
        identity.setId(FOO);
        identity.newDevice(NAME2);
        new ProposeSend(activity, identity, true).getProposed();

        assertEquals(R.string.err_refused, errorMessage);
    }

    /**
     * Correct error message is thrown when a ProposeSend request
     * polls an inexistent identity.
     */
    @Test
    public void cothorityThrowsCorrectErrorMessageForInexistentIdentity() {
        Identity identity = new Identity(NAME1, cothority(HOST, PORT));
        identity.setId(FOO);
        identity.newDevice(NAME2);
        new ProposeSend(activity, identity, true).getProposed();

        assertEquals(R.string.err_propose_send, errorMessage);
    }
}
