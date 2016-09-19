package com.epfl.dedis.cisc;

// TODO: COMMENT ALL THE FILES!!! (JAVA DOC STYLE)
// TODO: RECOMMENDED! Find new way to do API unit tests
public interface Activity {
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    void taskJoin();
    void taskFail(int error);
}