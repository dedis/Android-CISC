package com.epfl.dedis.api;

import com.epfl.dedis.cisc.Activity;
import com.epfl.dedis.cisc.R;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ConfigUpdate implements Request {

    private class ConfigUpdateMessage {
        @SerializedName("ID")
        String id;
    }

    private Activity mActivity;
    private Identity mIdentity;
    private Config mConfig;

    public ConfigUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ConfigUpdate(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;
        this.mIdentity = identity;

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
        mConfig = Utils.fromJson(result, Config.class);

        if (mConfig.getData() == null) {
            mConfig.setData(new HashMap<String, String>());
        }

        mIdentity.setConfig(mConfig);
        mActivity.taskJoin();
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

    public Config getConfig() {
        return mConfig;
    }
}