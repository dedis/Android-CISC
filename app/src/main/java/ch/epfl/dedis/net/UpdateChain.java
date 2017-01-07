package ch.epfl.dedis.net;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class UpdateChain {

    private static final String TAG = "net.UpdateChain";

    @SerializedName("Update")
    SkipBlock[] mChain;

    public boolean verifySkipChain() {
        Log.d(TAG, "Verify skipchain");

        // Separate first block which does not have a back link.
        if (!mChain[0].verifyBlock()) {
            return false;
        }

        // Verify alignment as well as as regular block integrity.
        for (int i = 1; i < mChain.length; i++) {
            if (!mChain[i].verifyBlock() || !verifiyAlignment(i)) {
                return false;
            }
        }

        return true;
    }

    private boolean verifiyAlignment(int index) {
        return mChain[index-1].getId().equals(mChain[index].getFix().getBack());
    }
}