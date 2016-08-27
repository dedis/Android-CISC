package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;

public class ConfigUpdate implements Message {

    private class ConfigUpdateMessage{
        int[] id;
        Config accountList;
    }

    private Identity identity;
    private Activity activity;

    public ConfigUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ConfigUpdate(Activity activity, Identity identity, boolean wait) {
        this.identity = identity;
        this.activity = activity;

        ConfigUpdateMessage configUpdateMessage = new ConfigUpdateMessage();
        byte[] id = identity.getSkipchainId();
        configUpdateMessage.id = Utils.byteArrayToIntArray(id);
        configUpdateMessage.accountList = null;

        HTTP http = new HTTP(this, identity.getCothority(), CONFIG_UPDATE, Utils.GSON.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": activity.callbackError(R.string.err_config_update); break;
            case "2": activity.callbackError(R.string.err_refused); break;
            default: {
                identity.setConfig(Utils.GSON.fromJson(result, Config.class));
                activity.callbackSuccess();
            }
        }
    }

    public Identity getIdentity() {
        return identity;
    }
}