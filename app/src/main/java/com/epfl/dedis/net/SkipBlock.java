package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class SkipBlock {

    @SerializedName("SkipBlockFix")
    public SkipBlockFix mFix;

    @SerializedName("Hash")
    private String mId;

    @SerializedName("Sig")
    private String mSig;

    @SerializedName("Msg")
    private String mMsg;

    public SkipBlockFix getFix() {
        return mFix;
    }

    public String getId() {
        return mId;
    }

    public String getSig() {
        return mSig;
    }

    public String getMsg() {
        return mMsg;
    }
}
