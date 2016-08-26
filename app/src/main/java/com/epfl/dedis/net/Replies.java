package com.epfl.dedis.net;

public interface Replies {

    // Log keys for SharedPreferences
    String PREFERENCES = "PREFERENCES";
    String HOST = "HOST";
    String PORT = "PORT";
    String ID = "ID";

    void callbackSuccess(String result);
    void callbackError(int error);
}