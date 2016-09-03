package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ProposeUpdateTest extends APITest{

    /**
     * In case a Skipchain does not contain a proposed configuration,
     * none is returned.
     */
    @Test
    public void cothorityReturnsNullForInexistentProposed() {
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        Config proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertNull(proposed);
    }

    /**
     * Polling a Skipchain that contains a fresh proposed configuration
     * for a recently added device. The ProposeUpdate request needs to
     * correspond to the one that was sent in a ProposeSend request.
     */
    @Test
    public void cothorityReturnsValidProposedForExistingProposed() {
        // Addding a device to a Skipchain then polling the proposed configuration
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

    /**
     * Correct error message is returned when sending a ProposeUpdate request
     * that polls an inexistent Identity.
     */
    @Test
    public void cothorityThrowsCorrectErrorMessageForInexistentIdentity() {
        new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true);
        Identity identity = new Identity(cothority(HOST, PORT), FOO);
        Config proposed = new ProposeUpdate(activity, identity, true).getProposed();

        assertEquals(R.string.err_config_update, errorMessage);
        assertNull(proposed);
    }

    /**
     * Correct error message is thrown when sending a ProposeUpdate request
     * containing an invalid network address.
     */
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
