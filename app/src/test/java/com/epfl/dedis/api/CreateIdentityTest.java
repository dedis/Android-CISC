package com.epfl.dedis.api;

import com.epfl.dedis.api.CreateIdentity;
import com.epfl.dedis.cisc.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class CreateIdentityTest extends APITest {

    @Test
    public void cothorityReturnsValidSkipchainIdentity() {
        CreateIdentity ci = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true);
        assertNotNull(ci.getIdentity().getSkipchainId());
        assertEquals(32, ci.getIdentity().getSkipchainId().length);
    }

    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        new CreateIdentity(activity, NAME1, cothority("foo", PORT), true);
        assertEquals(R.string.err_refused, errorMessage);
        new CreateIdentity(activity, NAME1, cothority(HOST, "1234"), true);
        assertEquals(R.string.err_refused, errorMessage);
    }

    @Test
    public void cothorityReturnsNoSkipchainIdentityForWrongAddress() {
        CreateIdentity ci = new CreateIdentity(activity, NAME1, cothority("foo", PORT), true);
        assertNull(ci.getIdentity().getSkipchainId());
    }
}
