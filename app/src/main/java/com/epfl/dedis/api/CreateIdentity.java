package com.epfl.dedis.api;

import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.epfl.dedis.net.Replies;

public class CreateIdentity implements Message {

    private class CreateIdentityMessage {
        private Config config;
    }

    private Replies app;
    private Identity identity;

    public CreateIdentity(Replies app, Cothority cot){
        this(app, cot, false);
    }

    public CreateIdentity(Replies app, Cothority cot, boolean wait) {
        this.app = app;
        this.identity = new Identity(DEVICE, cot);

        HTTP http = new HTTP(this, identity.getCothority(), ADD_IDENTITY, toJSON());
        if (wait) {
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
                identity.setSkipchainId(Utils.GSON.fromJson(result, byte[].class));
                app.callbackSuccess(result);
            }
        }
    }

    public String toJSON() {
        CreateIdentityMessage cim = new CreateIdentityMessage();
        cim.config = identity.getConfig();
        return Utils.GSON.toJson(cim);
    }

    public Identity getIdentity() {
        return identity;
    }
}
