package com.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class UtilsTest {

    /**
     * All types of byte arrays have to be correctly converted to
     * integer arrays.
     */
    @Test
    public void byteArrayCorrectlyConvertedToIntArray() {
        int[] array = Utils.byteArrayToIntArray(new byte[]{1, 2, 3});
        assertArrayEquals(new int[]{1, 2, 3}, array);

        array = Utils.byteArrayToIntArray(new byte[]{-1, -2, -3});
        assertArrayEquals(new int[]{255, 254, 253}, array);

        array = Utils.byteArrayToIntArray(new byte[]{-1, 2, -3});
        assertArrayEquals(new int[]{255, 2, 253}, array);
    }

    /**
     * Make sure there is no UUID collision.
     */
    @Test
    public void assertNoUUIDCollision() {
        assertNotEquals(Utils.uuid(), Utils.uuid());
    }

    @Test
    public void test() {
        String s = new byte[]{1, 2, 3}.toString();
        System.out.println(s);
    }
}
