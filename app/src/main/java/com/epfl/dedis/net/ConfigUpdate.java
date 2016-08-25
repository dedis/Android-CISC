package com.epfl.dedis.net;

import com.epfl.dedis.cisc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigUpdate implements Message {
    private class ConfigUpdateMessage{
        int[] ID;
        Config AccountList;
    }

    private Identity identity;
    private Replies app;

    public ConfigUpdate(Replies app, Identity identity) {
        this(app, identity, false);
    }
    public ConfigUpdate(Replies app, Identity identity, boolean wait){
        this.identity = identity;
        this.app = app;
        ConfigUpdateMessage cum = new ConfigUpdateMessage();
        byte[] id = identity.getSkipchainId();
        cum.ID = new int[id.length];
        for (int i = 0; i < id.length; i++){
            cum.ID[i] = id[i] & 0xff;
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        HTTP http = new HTTP(this, identity.getCothority(), CONFIG_UPDATE, gson.toJson(cum));
        if (wait){
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
                identity.setConfig(new Gson().fromJson(result, Config.class));
                app.callbackSuccess(result);
            }
        }
    }

    public Identity getIdentity() {
        return identity;
    }
}
