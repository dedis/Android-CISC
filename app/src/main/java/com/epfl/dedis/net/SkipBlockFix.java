package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class SkipBlockFix {

    @SerializedName("Index")
    public int mIndex;

    @SerializedName("Height")
    public int mHeight;

    @SerializedName("MaximumHeight")
    public int mMaximumHeight;

    @SerializedName("BaseHeight")
    public int mBaseHeight;

    @SerializedName("BackLinkIds")
    public String[] mBackLinkIds;

    @SerializedName("VerifierID")
    public String mVerifierId;

    @SerializedName("ParentBlockID")
    public String mParentBlockId;

    @SerializedName("Aggregate")
    public String mAggregate;

    @SerializedName("AggregateResp")
    public String mAggregateResp;

    @SerializedName("Data")
    public String mData;

    public String getBack() {
        System.out.println("-->" + mBackLinkIds.length);
        return mBackLinkIds[0];
    }

}
