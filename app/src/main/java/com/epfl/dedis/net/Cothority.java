package com.epfl.dedis.net;

public class Cothority {

    private String mHost;
    private String mPort;

    public Cothority(String host, String port){
        mHost = host;
        mPort = port;
    }

    public String getHost() {
        return mHost;
    }

    public String getPort() {
        return mPort;
    }
}
