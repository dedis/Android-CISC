package com.epfl.dedis.cisc;

public interface Activity {

    String DEVICE = "MOTOROLA";

    /* Log keys for SharedPreferences */
    String LOG = "LOG";
    String HOST = "HOST";
    String PORT = "PORT";
    String DATA = "DATA";
    String ID = "ID";
    String PUBLIC = "PUBLIC";
    String SECRET = "SECRET";

    /* Error Toasts */
    String ERR_REFUSED = "Connection refused";
    String ERR_NOT_FOUND = "Ed25519 not found";
    String ERR_EMPTY_FIELDS = "All fields must be filled";

    void toast(String text);
    void callback(String result);
}
