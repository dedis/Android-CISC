package com.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotEquals;

// TODO: Add more Utils tests
@RunWith(JUnit4.class)
public class UtilsTest {

    /**
     * Make sure there is no UUID collision.
     */
    @Test
    public void assertNoUUIDCollision() {
        assertNotEquals(Utils.uuid(), Utils.uuid());
    }
}
