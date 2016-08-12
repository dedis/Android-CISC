package com.epfl.dedis.cisc;

import com.epfl.dedis.net.HTTP;

import org.junit.Test;

import java.io.IOException;

public class HTTPTest {
    @Test(expected = IOException.class)
    public void exception_isThrown() throws Exception {
        throw new IOException();
    }
}
