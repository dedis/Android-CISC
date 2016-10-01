package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class SkipBlock {

    @SerializedName("Fix")
    private SkipBlockFix mFix;

    @SerializedName("Hash")
    private byte[] mId;

    @SerializedName("BlockSig")
    private String mBftSig;
}
