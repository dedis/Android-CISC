package ch.epfl.dedis.api;

import ch.epfl.dedis.cisc.Activity;
import ch.epfl.dedis.cisc.R;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Config;
import ch.epfl.dedis.net.HTTP;
import ch.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ConfigUpdate implements Request {

    private static final String PATH = "cu";

    private class ConfigUpdateMessage {
        @SerializedName("ID")
        String id;
    }

    private final Activity mActivity;
    private final Identity mIdentity;

    public ConfigUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ConfigUpdate(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;
        this.mIdentity = identity;

        ConfigUpdateMessage configUpdateMessage = new ConfigUpdateMessage();
        configUpdateMessage.id = Utils.encodeBase64(identity.getId());

        HTTP http = new HTTP(this, identity.getCothority(), PATH, Utils.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    public void callback(String result) {
        try {
            Config config = Utils.fromJson(result, Config.class);
            if (config.getData() == null) {
                config.setData(new HashMap<String, String>());
            }
            mIdentity.setConfig(config);
            mActivity.taskJoin();
        } catch (Exception e) {
            mActivity.taskFail(R.string.info_corruptedjson);
        }
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: mActivity.taskFail(R.string.err_400); break;
            case 500: mActivity.taskFail(R.string.err_500); break;
            case 501: mActivity.taskFail(R.string.err_501); break;
            case 502: mActivity.taskFail(R.string.err_502); break;
            case 503: mActivity.taskFail(R.string.err_503); break;
            case 504: mActivity.taskFail(R.string.err_504); break;
            default: mActivity.taskFail(R.string.err_unknown);
        }
    }
}