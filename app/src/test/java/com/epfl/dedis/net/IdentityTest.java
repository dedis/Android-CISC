package com.epfl.dedis.net;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class IdentityTest {

    @Test
    public void SaveId(){
        Identity id = new Identity("test");
        String save = id.save();
        Identity id2 = Identity.load(save);
        assertEquals(id.getPub(), id2.getPub());
        assertEquals(id.getPrivate(), id2.getPrivate());
        assertEquals(id.getName(), id2.getName());
    }
}
