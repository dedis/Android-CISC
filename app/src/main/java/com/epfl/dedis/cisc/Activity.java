package com.epfl.dedis.cisc;

public interface Activity {

    String LOG = "LOG";
    String HOST = "HOST";
    String PORT = "PORT";
    String DATA = "DATA";
    String ID = "ID";
    String PUBLIC = "PUBLIC";
    String SECRET = "SECRET";

    void toast(int text);
}
