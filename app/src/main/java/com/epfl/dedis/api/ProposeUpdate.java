package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ProposeUpdate implements Message{

    private class ProposeUpdateMessage{
        @SerializedName("ID")
        String id;
    }

    private Activity activity;
    private Identity identity;
    private Config proposed;

    public ProposeUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeUpdate(Activity activity, Identity identity, boolean wait) {
        this.activity = activity;
        this.identity = identity;

        ProposeUpdateMessage configUpdateMessage = new ProposeUpdateMessage();
        configUpdateMessage.id = Utils.encodeBase64(identity.getId());

        HTTP http = new HTTP(this, identity.getCothority(), PROPOSE_UPDATE, Utils.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        proposed = Utils.fromJson(result, Config.class);

        if (proposed.getData() == null) {
            proposed.setData(new HashMap<String, String>());
        }

        identity.setProposed(proposed);
        activity.taskJoin();
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: activity.taskFail(R.string.err_400); break;
            case 500: activity.taskFail(R.string.err_500); break;
            case 501: activity.taskFail(R.string.err_501); break;
            case 502: activity.taskFail(R.string.err_502); break;
            case 503: activity.taskFail(R.string.err_503); break;
            case 504: activity.taskFail(R.string.err_504); break;
            default: activity.taskFail(R.string.err_unknown);
        }
    }

    public Config getProposed() {
        return proposed;
    }
}
