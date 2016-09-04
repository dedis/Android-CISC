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
        String id;

        @SerializedName("Propose")
        Config propose;
    }

    private Activity activity;
    private Config proposed;

    public ProposeSend(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeSend(Activity activity, Identity identity, boolean wait) {
        this.activity = activity;

        ProposeSendMessage proposeSendMessage = new ProposeSendMessage();
        proposeSendMessage.id = Utils.encodeBase64(identity.getId());
        proposeSendMessage.propose = identity.getProposed();

        HTTP http = new HTTP(this, identity.getCothority(), PROPOSE_SEND, Utils.toJson(proposeSendMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        proposed = Utils.fromJson(result, Config.class);
        activity.taskJoin();
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: activity.taskFail(R.string.err_refused); break;
            case 500: activity.taskFail(R.string.err_refused); break;
            case 501: activity.taskFail(R.string.err_refused); break;
            case 502: activity.taskFail(R.string.err_propose_send); break;
            default: activity.taskFail(R.string.err_refused);
        }
    }

    // Not used - ProposeSend only returns OK or an error.
    public Config getProposed() {
        return proposed;
    }
}