package com.epfl.dedis.net;

public class AddIdentity implements Message {
    private Config Config;

    public AddIdentity(Config Config) {
        this.Config = Config;
    }
}
