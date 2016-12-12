package com.epfl.dedis.cisc;

public interface Activity {
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    int PERMISSION_CAMERA = 0;

    void taskJoin();
    void taskFail(int error);
}