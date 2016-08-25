package com.epfl.dedis.net;

public interface Replies {

    // Log keys for SharedPreferences
    String LOG = "LOG";
    String IDENTITY = "IDENTITY";

    void callbackSuccess(String result);
    void callbackError(int error);
}
