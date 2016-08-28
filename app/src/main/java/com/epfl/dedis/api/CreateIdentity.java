package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;

public class CreateIdentity implements Message {

    private class CreateIdentityMessage {
        Config config;
    }

    private Activity activity;
    private Identity identity;

    public CreateIdentity(Activity activity, String name, Cothority cothority){
        this(activity, name, cothority, false);
    }

    public CreateIdentity(Activity activity, String name, Cothority cothority, boolean wait) {
        this.activity = activity;
        this.identity = new Identity(name, cothority);

        HTTP http = new HTTP(this, identity.getCothority(), ADD_IDENTITY, toJson());
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": activity.callbackError(R.string.err_add_identity);
                break;
            case "2": activity.callbackError(R.string.err_refused);
                break;
            default: {
                identity.setSkipchainId(Utils.GSON.fromJson(result, byte[].class));
                activity.callbackSuccess();
            }
        }
    }

    public String toJson() {
        CreateIdentityMessage createIdentityMessage = new CreateIdentityMessage();
        createIdentityMessage.config = identity.getConfig();
        return Utils.GSON.toJson(createIdentityMessage);
    }

    public Identity getIdentity() {
        return identity;
    }
}
