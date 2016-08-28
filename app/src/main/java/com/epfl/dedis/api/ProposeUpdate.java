package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class ProposeUpdate implements Message{

    private class ProposeUpdateMessage{
        @SerializedName("ID")
        int[] id;

        @SerializedName("AccountList")
        Config accountList;
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
        configUpdateMessage.id = Utils.byteArrayToIntArray(identity.getSkipchainId());
        configUpdateMessage.accountList = null;

        HTTP http = new HTTP(this, identity.getCothority(), PROPOSE_UPDATE, Utils.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": activity.callbackError(R.string.err_config_update);
                break;
            case "2": activity.callbackError(R.string.err_refused);
                break;
            default: {
                proposed = Utils.fromJson(result, Config.class);
                identity.setProposed(proposed);
                activity.callbackSuccess();
            }
        }
    }

    public Config getProposed() {
        return proposed;
    }
}
