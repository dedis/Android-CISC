package com.epfl.dedis.cisc;

import com.epfl.dedis.net.Message;

public interface Thread {

    int ADD_IDENTITY = 0;
    int CONFIG_UPDATE = 1;

    String makeJson();
    Message parseJson(String json);
}
