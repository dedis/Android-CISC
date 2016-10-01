package com.epfl.dedis.net;

import com.google.gson.annotations.SerializedName;

public class UpdateChain {

    @SerializedName("Update")
    private SkipBlock[] mChain;
}
