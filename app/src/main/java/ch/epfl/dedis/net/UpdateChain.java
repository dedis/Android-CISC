package ch.epfl.dedis.net;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * The UpdateChain class contains the array of Skipblock making up
 * the Skipchain. It also used as the entry point for the verification
 * process.
 *
 * @author Andrea Caforio
 */
public class UpdateChain {

    private static final String TAG = "net.UpdateChain";

    @SerializedName("Update")
    SkipBlock[] mChain;

    /**
     * Three step verification process.
     *
     * 1.) Every block needs to contain the correct hash of the preceding
     * block as a backlink.
     * 2.) The identifier of each block needs to equal hash of all the Skipblock
     * fields.
     * 3.) The public aggregate key of each block needs to correspond to the block's
     * signature.
     *
     * @return true if all three steps can be verified
     */
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

    /**
     * Check backlink integrity.
     *
     * @param index of block in Skipchain
     * @return true if backlink equals identity of preceding block
     */
    private boolean verifiyAlignment(int index) {
        return mChain[index-1].getId().equals(mChain[index].getFix().getBack());
    }
}
