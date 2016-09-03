package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.net.Cothority;

import org.junit.BeforeClass;

public abstract class APITest {

    protected static final String HOST = "localhost";
    protected static final String PORT = "2000";
    protected static final String NAME1 = "test1";
    protected static final String NAME2 = "test2";
    protected static final String NAME3 = "test3";
    protected static final byte[] FOO = new byte[]{1, 2, 3};

    protected static Activity activity;
    protected static int errorMessage;

    /**
     * Called before any test file in this package. Setting up
     * a dummy Activity, only handling possible error messages.
     */
    @BeforeClass
    public static void setup() {
        activity = new Activity() {
            public void callbackSuccess() {}
            public void callbackError(int error) {
                errorMessage = error;
            }
        };
    }

    public Cothority cothority(String host, String port) {
        return new Cothority(host, port);
    }
}
