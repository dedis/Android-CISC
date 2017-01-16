package ch.epfl.dedis.api;

import android.util.Log;

import ch.epfl.dedis.cisc.Activity;
import ch.epfl.dedis.cisc.R;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Config;
import ch.epfl.dedis.net.HTTP;
import ch.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Request a potential proposed configuration from the Cothority by
 * sending a ProposeUpdateMessage JSON.
 *
 * @author Andrea Caforio
 */
public class ProposeUpdate implements Request {

    private static final String TAG = "api.ProposeUpdate";
    private static final String PATH = "pu";

    private final Activity mActivity;
    private final Identity mIdentity;

    public ProposeUpdate(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeUpdate(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;
        this.mIdentity = identity;

        ProposeUpdateMessage configUpdateMessage = new ProposeUpdateMessage();
        configUpdateMessage.id = Utils.encodeBase64(identity.getId());

        HTTP http = new HTTP(this, identity.getCothority(), PATH, Utils.toJson(configUpdateMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    /**
     * If currently no proposal is pending the Cothority will respond with
     * the "empty" string otherwise the response string is contains a Config
     * JSON that is then set in the Identity object.
     *
     * @param result response String from the Cothority
     */
    public void callback(String result) {
        try {
            if (result.equals("empty")) {
                mIdentity.setProposed(null);
            } else {
                Config proposed = Utils.fromJson(result, Config.class);
                // Allocate new data map if proposal doesn't contain any.
                if (proposed.getData() == null) {
                    proposed.setData(new HashMap<String, String>());
                }
                mIdentity.setProposed(proposed);
            }
            mActivity.taskJoin();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            mActivity.taskFail(R.string.info_corruptedjson);
        }
    }

    public void callbackError(int error) {
        switch (error) {
            case 400: mActivity.taskFail(R.string.err_400); break;
            case 500: mActivity.taskFail(R.string.err_500); break;
            case 501: mActivity.taskFail(R.string.err_501); break;
            case 502: mActivity.taskFail(R.string.err_502); break;
            case 504: mActivity.taskFail(R.string.err_504); break;
            default: mActivity.taskFail(R.string.err_unknown);
        }
    }

    /**
     * Requesting a proposal solely consists of emitting the identity hash
     * of the Cothority.
     *
     * @author Andrea Caforio
     */
    private class ProposeUpdateMessage{
        @SerializedName("ID")
        String id;
    }
}
