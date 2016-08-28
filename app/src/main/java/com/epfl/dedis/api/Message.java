package com.epfl.dedis.api;

public interface Message {
    // Device name
    String DEVICE = "MOTOROLA";

    // Cothority API paths
    String ADD_IDENTITY = "ai";
    String CONFIG_UPDATE = "cu";
    String PROPOSE_SEND = "ps";
    String PROPOSE_UPDATE = "pu";

    void callback(String result);

}
