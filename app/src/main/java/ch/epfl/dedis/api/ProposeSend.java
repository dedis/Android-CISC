package ch.epfl.dedis.api;

import ch.epfl.dedis.cisc.Activity;
import ch.epfl.dedis.cisc.R;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Config;
import ch.epfl.dedis.net.HTTP;
import ch.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

public class ProposeSend implements Request {

    private static final String PATH = "ps";

    private class ProposeSendMessage {

        @SerializedName("ID")
        String id;

        @SerializedName("Propose")
        Config propose;
    }

    private final Activity mActivity;

    public ProposeSend(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeSend(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;

        ProposeSendMessage proposeSendMessage = new ProposeSendMessage();
        proposeSendMessage.id = Utils.encodeBase64(identity.getId());
        proposeSendMessage.propose = identity.getProposed();

        HTTP http = new HTTP(this, identity.getCothority(), PATH, Utils.toJson(proposeSendMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    @SuppressWarnings("unused")
    public void callback(String result) {
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
}