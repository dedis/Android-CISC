package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import net.i2p.crypto.eddsa.EdDSAEngine;

public class ProposeVote implements Message {

    // TODO: Check if private inner classes need private fields and constructor
    private class ProposeVoteMessage {
        @SerializedName("ID")
        String id;

        @SerializedName("Signer")
        String signer;

        @SerializedName("Signature")
        String signature;
    }

    private Activity mActivity;

    public ProposeVote(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeVote(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;

        ProposeVoteMessage proposeVoteMessage = new ProposeVoteMessage();
        proposeVoteMessage.id = Utils.encodeBase64(identity.getId());
        proposeVoteMessage.signer = identity.getName();

        try {
            EdDSAEngine engine = new EdDSAEngine();
            engine.initSign(identity.getPrivate());
            proposeVoteMessage.signature = Utils.encodeBase64(engine.signOneShot(identity.getProposed().hash()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HTTP http = new HTTP(this, identity.getCothority(), PROPOSE_VOTE, Utils.toJson(proposeVoteMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {}

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
