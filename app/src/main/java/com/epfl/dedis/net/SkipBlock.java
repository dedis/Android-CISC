package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class SkipBlock {

    @SerializedName("SkipBlockFix")
    public SkipBlockFix mFix;

    @SerializedName("Hash")
    private String mId;

    @SerializedName("Sig")
    private String sig;

    @SerializedName("Msg")
    private String msg;

    public SkipBlockFix getFix() {
        return mFix;
    }

    public String getId() {
        return mId;
    }
}
