package ch.epfl.dedis.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void correctyPerformBase64Conversions() {
        byte[] msg = {1, 2, 3};
        String encoding = Utils.encodeBase64(msg);

        assertEquals("AQID", encoding);
        assertArrayEquals(msg, Utils.decodeBase64(encoding));
    }

    @Test
    public void subsequentIdentifiersDoNotCollide() {
        String uid1 = Utils.uuid();
        String uid2 = Utils.uuid();

        assertNotEquals(uid1, uid2);
    }
}
