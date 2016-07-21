package com.epfl.dedis.net;

public class ConfigUpdate extends Message {

    private byte[] id;
    private Config accountList;

    public ConfigUpdate(int type, byte[] id, Config accountList) {
        super(type);
        this.id = id;
        this.accountList = accountList;
    }
}
