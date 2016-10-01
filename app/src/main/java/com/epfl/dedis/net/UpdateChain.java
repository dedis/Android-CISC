package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class UpdateChain {

    @SerializedName("Update")
    private SkipBlock[] mChain;

    public SkipBlock[] getChain() {
        return mChain;
    }

    public boolean verifySkipChain() {
        for (SkipBlock sb : mChain) {
            if (!sb.verifyBlock()) {
                return false;
            }
        }
        return true;
    }
}
