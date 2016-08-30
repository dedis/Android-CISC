package com.epfl.dedis.net;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HTTPTest {

    @Test
    public void throwCorrectErrorStringForWrongAddress() {
        HTTP http = new HTTP(null, new Cothority("0", "1"), "foo", "bar");
        String error = http.doInBackground();
        assertEquals("2", error);
    }
}
