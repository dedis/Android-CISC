package com.epfl.dedis.net;

public class Cothority {
    private String host;
    private String port;

    public Cothority(String host, String port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

}
