package com.epfl.dedis.cisc;

public interface Activity {

    // Log keys for SharedPreferences
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";
    String HOST = "HOST";
    String PORT = "PORT";
    String ID = "ID";

    void callbackSuccess();
    void callbackError(int error);
}