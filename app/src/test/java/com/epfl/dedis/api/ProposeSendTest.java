package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ProposeSendTest extends APITest {

    @Test
    public void cothorityReturnsValidProposedForNewDevice() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.newDevice(NAME2);
        int priorThreshold = identity.getProposed().getThreshold();
        String priorPublicKey = identity.getProposed().getDevice().get(NAME2);
        Config proposed = new ProposeSend(activity, identity, true).getProposed();

        assertEquals(priorThreshold, proposed.getThreshold());
        assertEquals(priorPublicKey, proposed.getDevice().get(NAME2));
        assertEquals(2, proposed.getDevice().size());
    }

    @Test
    public void cothorityReturnsValidProposedForDataUpdate() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.updateData("test data");
        String priorData = identity.getProposed().getData().get(NAME1);
        Config proposed = new ProposeSend(activity, identity, true).getProposed();

        assertEquals(priorData, proposed.getData().get(NAME1));
        assertEquals(1, proposed.getData().size());
    }

    @Test
    public void keyPairMatchingAfterSuccessfulConfigUpdate() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.newDevice(NAME2);
        Config proposed = new ProposeSend(activity, identity, true).getProposed();

        assertEquals(Ed25519.PubString(identity.getPub()), proposed.getDevice().get(NAME2));
    }

    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        Identity identity = new Identity(NAME1, cothority("foo", PORT));
        identity.setSkipchainId(FOO);
        identity.newDevice(NAME2);
        new ProposeSend(activity, identity, true).getProposed();

        assertEquals(R.string.err_refused, errorMessage);
    }

    @Test
    public void cothorityThrowsCorrectErrorMessageForInexistentIdentity() {
        Identity identity = new Identity(NAME1, cothority(HOST, PORT));
        identity.setSkipchainId(FOO);
        identity.newDevice(NAME2);
        new ProposeSend(activity, identity, true).getProposed();

        assertEquals(R.string.err_propose_send, errorMessage);
    }
}
