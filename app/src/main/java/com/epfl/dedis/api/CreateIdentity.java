package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class CreateIdentity implements Message {

    private class CreateIdentityMessage {
        @SerializedName("Config")
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

        CreateIdentityMessage createIdentityMessage = new CreateIdentityMessage();
        createIdentityMessage.config = identity.getConfig();

        HTTP http = new HTTP(this, identity.getCothority(), ADD_IDENTITY, Utils.toJson(createIdentityMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        switch (result) {
            case "1": activity.taskFail(R.string.err_add_identity);
                break;
            case "2": activity.taskFail(R.string.err_refused);
                break;
            default: {
                identity.setId(Utils.decodeBase64(result));
                activity.taskJoin();
            }
        }
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

    public Identity getIdentity() {
        return identity;
    }
}
