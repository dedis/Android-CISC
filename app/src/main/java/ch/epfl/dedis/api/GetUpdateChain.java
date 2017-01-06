package ch.epfl.dedis.api;

import ch.epfl.dedis.cisc.Activity;
import ch.epfl.dedis.cisc.R;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.HTTP;
import ch.epfl.dedis.net.Identity;
import ch.epfl.dedis.net.UpdateChain;
import com.google.gson.annotations.SerializedName;

public class GetUpdateChain implements Request {

    private Activity mActivity;
    private Identity mIdentity;

    private class GetUpdateChainMessage {
        @SerializedName("LatestID")
        String latestId;
    }

    public GetUpdateChain(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public GetUpdateChain(Activity activity, Identity identity, boolean wait) {
        mActivity = activity;
        mIdentity = identity;

        GetUpdateChainMessage getUpdateChainMessage = new GetUpdateChainMessage();
        getUpdateChainMessage.latestId = Utils.encodeBase64(mIdentity.getId());

        HTTP http = new HTTP(this, identity.getCothority(), GET_UPDATE_CHAIN, Utils.toJson(getUpdateChainMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        try {
            UpdateChain uc = Utils.fromJson(result, UpdateChain.class);
            boolean verified = uc.verifySkipChain(Utils.encodeBase64(mIdentity.getId()));
            if (verified) {
                mActivity.taskJoin();
            } else {
                mActivity.taskFail(R.string.info_noverification);
            }
        } catch (Exception e) {
            mActivity.taskFail(R.string.info_corruptedjson);
        }
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: mActivity.taskFail(R.string.err_400); break;
            case 500: mActivity.taskFail(R.string.err_500); break;
            case 501: mActivity.taskFail(R.string.err_501); break;
            case 502: mActivity.taskFail(R.string.err_502); break;
            case 503: mActivity.taskFail(R.string.err_503); break;
            case 504: mActivity.taskFail(R.string.err_504); break;
            default: mActivity.taskFail(R.string.err_unknown);
        }
    }
}
