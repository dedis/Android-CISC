package com.epfl.dedis.cisc;

public interface Activity {

    String NAME = "MOTOROLA";

    // Log keys for SharedPreferences
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";
    String HOST = "HOST";
    String PORT = "PORT";
    String ID = "ID";

    void taskJoin();
    void taskFail(int error);
}