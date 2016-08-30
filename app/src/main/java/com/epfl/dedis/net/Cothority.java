package com.epfl.dedis.net;

public class Cothority {
    private String _host;
    private String _port;

    public Cothority(String host, String port){
        _host = host;
        _port = port;
    }

    public String getHost() {
        return _host;
    }

    public String getPort() {
        return _port;
    }
}
