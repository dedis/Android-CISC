package com.epfl.dedis.cisc;

public interface Activity {
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    void taskJoin();
    void taskFail(int error);
}