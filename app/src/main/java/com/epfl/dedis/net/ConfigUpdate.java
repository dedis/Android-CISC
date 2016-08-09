package com.epfl.dedis.net;

public class ConfigUpdate extends Message {

    private int[] ID;
    private Config AccountList;

    public ConfigUpdate(int[] ID, Config AccountList) {
        this.ID = ID;
        this.AccountList = AccountList;
    }
}
