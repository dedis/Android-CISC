package com.epfl.dedis.cisc;

import com.epfl.dedis.crypto.Identity;

import org.junit.Test;

import static org.junit.Assert.*;

public class IdentityTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void generation_isDifferent() throws Exception {
        Identity id1 = new Identity();
        Identity id2 = new Identity();
        assertNotEquals(id1.toString(), id2.toString());
    }
}