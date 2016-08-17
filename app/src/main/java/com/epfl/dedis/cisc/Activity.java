package com.epfl.dedis.cisc;

public interface Activity {

    // Device name
    String DEVICE = "MOTOROLA";

    // Log keys for SharedPreferences
    String LOG = "LOG";
    String HOST = "HOST";
    String PORT = "PORT";
    String DATA = "DATA";
    String ID = "ID";
    String PUBLIC = "PUBLIC";
    String SECRET = "SECRET";

    // Cothority API paths
    String ADD_IDENTITY = "ai";
    String CONFIG_UPDATE = "cu";
    String PROPOSE_SEND = "ps";

    void callback(String result);
}
