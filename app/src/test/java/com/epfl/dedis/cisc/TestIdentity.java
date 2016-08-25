package com.epfl.dedis.cisc;

import com.epfl.dedis.net.Identity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.RobolectricGradleTestRunner;

import static org.junit.Assert.*;
/**
 * Created by Andrea on 25/08/16.
 */
@RunWith(JUnit4.class)
public class TestIdentity {

    @Test
    public void SaveId(){
        Identity id = new Identity("test");
        String save = id.save();
        Identity id2 = Identity.load(save);
        assertEquals(id.getPub(), id2.getPub());
        assertEquals(id.getPrivate(), id2.getPrivate());
        assertEquals(id.getDeviceName(), id2.getDeviceName());
    }

}
