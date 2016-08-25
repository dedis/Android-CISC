package com.epfl.dedis.net;

public interface Message {
    // Device name
    String DEVICE = "MOTOROLA";

    // Log keys for SharedPreferences
    String LOG = "LOG";
    String IDENTITY = "IDENTITY";

    // Cothority API paths
    String ADD_IDENTITY = "ai";
    String CONFIG_UPDATE = "cu";
    String PROPOSE_SEND = "ps";

    void callback(String result);

}
