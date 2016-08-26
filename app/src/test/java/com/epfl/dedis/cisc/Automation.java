package com.epfl.dedis.cisc;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.api.CreateIdentity;
import com.epfl.dedis.net.Identity;
import com.epfl.dedis.net.Replies;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class Automation {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";

    private static Identity identity;
    private static Replies replies;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        replies = new Replies(){
            public void callbackSuccess(String result){
                System.out.println(result);
            }
            public void callbackError(int error){
                Assert.fail("Error while contacting cothority");
            }
        };

        Cothority cot = new Cothority(HOST, PORT);
        CreateIdentity id = new CreateIdentity(replies, cot, true);
        identity = id.getIdentity();
    }

    @Test
    public void createActivityAddIdentity(){
        assertEquals("test", identity.getDeviceName());
        assertEquals(identity.getPub(), identity.getConfig().getDevice().get("test"));
    }

    @Test
    public void mainActivityConfigUpdateExistingIdentity() {
        System.out.println(Arrays.toString(identity.getSkipchainId()));
        ConfigUpdate cu = new ConfigUpdate(replies, identity, true);
        Identity id2 = cu.getIdentity();
        assertEquals(identity.getPub(), id2.getPub());
        assertEquals(identity.getSkipchainId(), id2.getSkipchainId());
    }
}