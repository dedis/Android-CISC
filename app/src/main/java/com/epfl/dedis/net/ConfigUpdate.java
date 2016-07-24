package com.epfl.dedis.net;

public class ConfigUpdate extends Message {

    private String ID;
    private Config AccountList;

    public ConfigUpdate(String ID, Config AccountList) {
        this.ID = ID;
        this.AccountList = AccountList;
    }
}
