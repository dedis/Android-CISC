package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class SkipBlockFix {

    @SerializedName("Index")
    int mIndex;

    @SerializedName("Height")
    int mHeight;

    @SerializedName("MaximumHeight")
    int mMaximumHeight;

    @SerializedName("BaseHeight")
    int mBaseHeight;

    @SerializedName("BackLinkIds")
    String[] mBackLinkIds;

    @SerializedName("VerifierID")
    String mVerifierId;

    @SerializedName("ParentBlockID")
    String mParentBlockId;

    @SerializedName("Aggregate")
    String mAggregate;

    @SerializedName("AggregateResp")
    String mAggregateResp;

    @SerializedName("Data")
    String mData;

    public String getBack() {
        return mBackLinkIds[0];
    }
}
