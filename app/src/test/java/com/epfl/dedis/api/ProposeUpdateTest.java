package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ProposeUpdateTest extends APITest{

    @Test
    public void cothorityReturnsNullForInexistentProposed() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        Config proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertNull(proposed);
    }

    @Test
    public void cothorityReturnsValidProposedForExistingProposed() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        identity.newDevice(NAME2);
        Config proposed1 = new ProposeSend(activity, identity, true).getProposed();
        identity.setProposed(null);
        Config proposed2 = new ProposeUpdate(activity, identity, true).getProposed();

        assertEquals(identity.getConfig().getDevice().get(NAME1), proposed2.getDevice().get(NAME1));
        assertEquals(proposed1.getDevice().get(NAME1), proposed2.getDevice().get(NAME1));
        assertEquals(proposed1.getDevice().get(NAME2), proposed2.getDevice().get(NAME2));
        assertEquals(2, proposed2.getDevice().size());
    }


    @Test
    public void cothorityThrowsCorrectErrorMessageForInexistentIdentity() {
        new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true);
        Identity identity = new Identity(cothority(HOST, PORT), FOO);
        Config proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertEquals(R.string.err_config_update, errorMessage);
        assertNull(proposed);
    }

    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        Identity identity = new Identity(cothority("foo", PORT), FOO);
        Config proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertEquals(R.string.err_refused, errorMessage);
        assertNull(proposed);

        identity = new Identity(cothority("foo", PORT), FOO);
        proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertEquals(R.string.err_refused, errorMessage);
        assertNull(proposed);
    }
}
