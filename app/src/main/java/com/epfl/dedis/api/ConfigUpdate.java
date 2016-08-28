package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class ConfigUpdate implements Message {

    private class ConfigUpdateMessage{
        @SerializedName("ID")
        int[] id;

        @SerializedName("AccountList")
        Config accountList;
    }

    private Activity activity;
    private Identity identity;
    private Config config;

    public ConfigUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ConfigUpdate(Activity activity, Identity identity, boolean wait) {
        this.activity = activity;
        this.identity = identity;

        ConfigUpdateMessage configUpdateMessage = new ConfigUpdateMessage();
        configUpdateMessage.id = Utils.byteArrayToIntArray(identity.getSkipchainId());
        configUpdateMessage.accountList = null;

        HTTP http = new HTTP(this, identity.getCothority(), CONFIG_UPDATE, Utils.toJson(configUpdateMessage));
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
                config = Utils.fromJson(result, Config.class);
                identity.setConfig(config);
                activity.callbackSuccess();
            }
        }
    }

    public Config getConfig() {
        return config;
    }
}