package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class ProposeSend implements Message {

    private class ProposeSendMessage {

        @SerializedName("ID")
        int[] id;

        @SerializedName("Config")
        Config config;
    }

    private Activity activity;
    private Config proposed;

    public ProposeSend(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeSend(Activity activity, Identity identity, boolean wait) {
        this.activity = activity;

        ProposeSendMessage proposeSendMessage = new ProposeSendMessage();
        proposeSendMessage.id = Utils.byteArrayToIntArray(identity.getSkipchainId());
        proposeSendMessage.config = identity.getProposed();

        HTTP http = new HTTP(this, identity.getCothority(), PROPOSE_SEND, Utils.toJson(proposeSendMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": activity.callbackError(R.string.err_propose_send);
                break;
            case "2": activity.callbackError(R.string.err_refused);
                break;
            default: {
                proposed = Utils.fromJson(result, Config.class);
                activity.callbackSuccess();
            }
        }
    }

    public Config getProposed() {
        return proposed;
    }
}