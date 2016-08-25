package com.epfl.dedis.net;

import com.epfl.dedis.cisc.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreateIdentity implements Message {
    private class CreateIdentityMessage {
        private Config Config;
    }

    private Replies app;
    private Identity identity;

    public CreateIdentity(Replies app, Cothority cot){
        this(app, cot, false);
    }

    public CreateIdentity(Replies app, Cothority cot, boolean wait) {
        this.app = app;
        this.identity = new Identity("test", cot);
        HTTP http = new HTTP(this, identity.getCothority(), ADD_IDENTITY, toJSON());
        if (wait){
            String result = http.doInBackground();
            http.onPostExecute(result);

        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": app.callbackError(R.string.err_add_identity); break;
            case "2": app.callbackError(R.string.err_refused); break;
            default: {
                identity.setSkipchainId(new Gson().fromJson(result, byte[].class));
                app.callbackSuccess(result);
            }
        }
    }

    public String toJSON() {
        CreateIdentityMessage cim = new CreateIdentityMessage();
        cim.Config = identity.getConfig();
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(cim);
    }

    public Identity getIdentity() {
        return identity;
    }
}
