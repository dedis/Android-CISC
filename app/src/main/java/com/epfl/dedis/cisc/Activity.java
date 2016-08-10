package com.epfl.dedis.cisc;

public interface Activity {

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
    String ERR_NOT_FOUND = "Identity not found";
    String ERR_EMPTY_FIELDS = "All fields must be filled";
    String ERR_ADD_IDENTITY = "Skipchain creation unsuccessful";

    void toast(String text);
}
