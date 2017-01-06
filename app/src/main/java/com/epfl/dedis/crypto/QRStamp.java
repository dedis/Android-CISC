package com.epfl.dedis.crypto;

import com.google.gson.annotations.SerializedName;

public class QRStamp {

    @SerializedName("ID")
    String mId;

    @SerializedName("Host")
    String mHost;

    @SerializedName("Port")
    String mPort;

    public QRStamp(String id, String host, String port) {
        this.mId = id;
        this.mHost = host;
        this.mPort = port;
    }

    public String getId() {
        return mId;
    }

    public String getHost() {
        return mHost;
    }

    public String getPort() {
        return mPort;
    }
}
