package com.epfl.dedis.cisc;

// TODO: COMMENT ALL THE FILES!!! (JAVA DOC STYLE)
public interface Activity {
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    void taskJoin();
    void taskFail(int error);
}