package com.epfl.dedis.net;

import java.util.Arrays;

public class ConfigUpdate implements Message {

    private int[] ID;
    private Config AccountList;

    public ConfigUpdate(int[] ID, Config AccountList) {
        this.ID = ID;
        this.AccountList = AccountList;
    }

    public int[] getID() {
        return ID;
    }

    public Config getAccountList() {
        return AccountList;
    }
}
