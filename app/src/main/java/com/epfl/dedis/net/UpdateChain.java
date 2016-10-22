package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class UpdateChain {

    @SerializedName("Update")
    private SkipBlock[] mChain;

    public SkipBlock[] getChain() {
        return mChain;
    }

    public boolean verifySkipChain() {
        System.out.println(mChain.length);
        for (SkipBlock sb : mChain) {
            System.out.println(sb.getId());
            System.out.println(sb.getFix().getBack());
            if (!sb.verifyBlock()) {
                return false;
            }
        }
        return true;
    }
}
