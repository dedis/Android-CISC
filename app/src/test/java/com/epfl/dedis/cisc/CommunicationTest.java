package com.epfl.dedis.cisc;

import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.lang.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommunicationTest {

    private static final String HOST = "127.0.0.1";
    private static final String PORT = "2000";

    private Gson gson = new GsonBuilder().serializeNulls().create();

    static String id;

    @Test
    public void AddIdentityTest() throws Exception {
        CreateActivity ca = new CreateActivity();
        String json = ca.makeJson();
        String ack = HTTP.open(HOST, PORT, "ai", json);
        assertNotEquals(ack, "");
        id = ack;

        int[] idArray = gson.fromJson(id, int[].class);
        assertEquals(idArray.length, 32);
    }

    @Test
    public void ConfigUpdateTest() throws Exception {
        //java.lang.Thread.sleep(1000);
        int[] idArray = gson.fromJson(id, int[].class);
        ConfigUpdate cu = new ConfigUpdate(idArray, null);

        String ack = HTTP.open(HOST, PORT, "cu", gson.toJson(cu));
        assertNotEquals(ack, "");
    }
}