package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.epfl.dedis.net.Replies;

public class ConfigUpdate implements Message {
    private class ConfigUpdateMessage{
        int[] id;
        Config accountList;
    }

    private Identity identity;
    private Replies app;

    public ConfigUpdate(Replies app, Identity identity) {
        this(app, identity, false);
    }

    public ConfigUpdate(Replies app, Identity identity, boolean wait) {
        this.identity = identity;
        this.app = app;

        ConfigUpdateMessage cum = new ConfigUpdateMessage();
        byte[] id = identity.getSkipchainId();
        cum.id = Utils.byteArrayToIntArray(id);

        HTTP http = new HTTP(this, identity.getCothority(), CONFIG_UPDATE, Utils.GSON.toJson(cum));

        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": app.callbackError(R.string.err_config_update); break;
            case "2": app.callbackError(R.string.err_refused); break;
            default: {
                identity.setConfig(Utils.GSON.fromJson(result, Config.class));
                app.callbackSuccess(result);
            }
        }
    }

    public Identity getIdentity() {
        return identity;
    }
}
