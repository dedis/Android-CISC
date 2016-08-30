package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class IdentityTest {

    @Test
    public void SaveId(){
        Identity id = new Identity("test", new Cothority(null, null));
        String save = Utils.toJson(id);
        Identity id2 = Utils.fromJson(save, Identity.class);
        assertEquals(id.getPublic(), id2.getPublic());
        assertEquals(id.getPrivate(), id2.getPrivate());
        assertEquals(id.getName(), id2.getName());
    }
}
