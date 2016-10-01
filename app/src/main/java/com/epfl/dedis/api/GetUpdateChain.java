package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.epfl.dedis.net.UpdateChain;
import com.google.gson.annotations.SerializedName;

public class GetUpdateChain implements Request {

    private Activity mActivity;
    private Identity mIdentity;

    private class GetUpdateChainMessage {
        @SerializedName("LatestID")
        private String latestId;
    }

    public GetUpdateChain(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public GetUpdateChain(Activity activity, Identity identity, boolean wait) {
        mActivity = activity;
        mIdentity = identity;

        GetUpdateChainMessage getUpdateChainMessage = new GetUpdateChainMessage();
        //getUpdateChainMessage.latestId = identity.getStringId();
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
        UpdateChain uc = Utils.fromJson(result, UpdateChain.class);
        System.out.println(uc.verifySkipChain());
    }

    // TODO: More or more detailed error messages; also for other Actitivies
    // TODO: Check if HTTP-error codes are suitable
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
