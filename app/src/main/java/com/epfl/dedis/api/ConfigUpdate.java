package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class ConfigUpdate implements Message {

    private class ConfigUpdateMessage {
        @SerializedName("ID")
        String id;
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
        configUpdateMessage.id = Utils.encodeBase64(identity.getId());

        HTTP http = new HTTP(this, identity.getCothority(), CONFIG_UPDATE, Utils.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        config = Utils.fromJson(result, Config.class);
        identity.setConfig(config);
        activity.taskJoin();
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: activity.taskFail(R.string.err_refused); break;
            case 500: activity.taskFail(R.string.err_refused); break;
            case 501: activity.taskFail(R.string.err_refused); break;
            case 502: activity.taskFail(R.string.err_config_update); break;
            default: activity.taskFail(R.string.err_refused);
        }
    }

    public Config getConfig() {
        return config;
    }
}