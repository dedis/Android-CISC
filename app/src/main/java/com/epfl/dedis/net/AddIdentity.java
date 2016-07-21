package com.epfl.dedis.net;

public class AddIdentity extends Message {
    private Config config;

    public AddIdentity(int type, Config config) {
        super(type);
        this.config = config;
    }
}
