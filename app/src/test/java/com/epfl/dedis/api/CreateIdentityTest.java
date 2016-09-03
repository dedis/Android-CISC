package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class CreateIdentityTest extends APITest {

    /**
     * When adding a new Identity to the Cothority the returned ID needs
     * to consist of 32 bytes.
     */
    @Test
    public void cothorityReturnsValidSkipchainIdentity() {
        CreateIdentity ci = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true);
        assertNotNull(ci.getIdentity().getId());
        assertEquals(32, ci.getIdentity().getId().length);
    }

    /**
     * In case of an invalid network address a corresponding error
     * has to be returned.
     */
    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        new CreateIdentity(activity, NAME1, cothority("foo", PORT), true);
        assertEquals(R.string.err_refused, errorMessage);
        new CreateIdentity(activity, NAME1, cothority(HOST, "1234"), true);
        assertEquals(R.string.err_refused, errorMessage);
    }
}
