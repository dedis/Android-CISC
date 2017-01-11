package ch.epfl.dedis.net;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class IdentityTest {

    private final Cothority mCothority = new Cothority("", "");

    @Test
    public void createJoinProposal() {
        Identity identity = new Identity("device", mCothority);
        identity.newDevice("device");
        Config proposed = identity.getProposed();

        assertTrue(proposed.getDevice().containsKey("device"));
    }

    @Test
    public void createDataProposal() {
        Identity identity = new Identity("device", mCothority);
        identity.updateData("data");
        Config proposed = identity.getProposed();

        assertEquals("data", proposed.getData().get("device"));
    }

    @Test
    public void returnNullForEmptyProposal() {
        Identity identity = new Identity("device", mCothority);
        assertNull(identity.getProposalString());
    }
}
