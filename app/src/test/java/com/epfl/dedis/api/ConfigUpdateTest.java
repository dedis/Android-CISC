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
public class ConfigUpdateTest extends APITest {

    /**
     * Set up new Skipchain then poll the configuration via ConfigUpdate.
     * Prior and posterior configuration have to be identical.
     */
    @Test
    public void cothorityReturnsValidConfigForExistingIdentity() {
        // Store the prior config when creating Identity
        Identity identity = new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true).getIdentity();
        int priorThreshold = identity.getConfig().getThreshold();
        String priorPublicKey = identity.getConfig().getDevice().get(NAME1);
        Config config = new ConfigUpdate(activity, identity, true).getConfig();

        assertEquals(priorThreshold, config.getThreshold());
        assertEquals(priorPublicKey, config.getDevice().get(NAME1));
        assertEquals(1, config.getDevice().size());
    }

    /**
     * Correct error is thrown for an inexistent identity and no
     * configuration is returned.
     */
    @Test
    public void cothorityThrowsCorrectErrorMessageForInexistentIdentity() {
        // Create new Identity then poll a wrong one
        new CreateIdentity(activity, NAME1, cothority(HOST, PORT), true);
        Identity identity = new Identity(cothority(HOST, PORT), FOO);
        Config config = new ConfigUpdate(activity, identity, true).getConfig();

        assertEquals(R.string.err_config_update, errorMessage);
        assertNull(config);
    }

    /**
     * Correct error is thrown for an invalid network address when
     * call ConfigUpdate.
     */
    @Test
    public void httpThrowsCorrectErrorMessageForWrongAddress() {
        Identity identity = new Identity(cothority("foo", PORT), FOO);
        Config config = new ConfigUpdate(activity, identity, true).getConfig();

        assertEquals(R.string.err_refused, errorMessage);
        assertNull(config);

        identity = new Identity(cothority("foo", PORT), FOO);
        config = new ConfigUpdate(activity, identity, true).getConfig();

        assertEquals(R.string.err_refused, errorMessage);
        assertNull(config);
    }
}
