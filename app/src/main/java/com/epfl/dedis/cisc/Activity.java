package com.epfl.dedis.cisc;

public interface Activity {

    // Log keys for SharedPreferences
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    String STATUS_INTENT = "STATUS";

    void taskJoin();
    void taskFail(int error);
}