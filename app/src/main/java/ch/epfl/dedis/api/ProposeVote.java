package ch.epfl.dedis.api;

import ch.epfl.dedis.cisc.Activity;
import ch.epfl.dedis.cisc.R;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.HTTP;
import ch.epfl.dedis.net.Identity;
import com.google.gson.annotations.SerializedName;

import net.i2p.crypto.eddsa.EdDSAEngine;

/**
 * Voting on a proposal signifies the emission of a
 * ProposeVoteMessage JSON to the Cothority.
 *
 * @author Andrea Caforio
 */
public class ProposeVote implements Request {

    private static final String PATH = "pv";

    private final Activity mActivity;

    public ProposeVote(Activity activity, Identity identity) {
        this(activity, identity, false);
    }

    public ProposeVote(Activity activity, Identity identity, boolean wait) {
        this.mActivity = activity;

        ProposeVoteMessage proposeVoteMessage = new ProposeVoteMessage();
        proposeVoteMessage.id = Utils.encodeBase64(identity.getId());
        proposeVoteMessage.signer = identity.getName();

        try {
            // Sign proposed configuration.
            EdDSAEngine engine = new EdDSAEngine();
            engine.initSign(identity.getPrivate());
            proposeVoteMessage.signature = Utils.encodeBase64(engine.signOneShot(identity.getProposed().hash()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HTTP http = new HTTP(this, identity.getCothority(), PATH, Utils.toJson(proposeVoteMessage));
        if (wait) {
            String result = http.doInBackground();
            http.onPostExecute(result);
        } else {
            http.execute();
        }
    }

    /**
     * A successful vote is marked by receving a non-empty string
     * from the Cothority which is then ignored by the callback procedure.
     *
     * @param result response String from the Cothority
     */
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

    /**
     * A voting message consists of the identity hash of the Skipchain,
     * the name of the signing device and the EdDSA signature of the proposed
     * configuration with the private key of the device.
     *
     * @author Andrea Caforio
     */
    private class ProposeVoteMessage {
        @SerializedName("ID")
        String id;

        @SerializedName("Signer")
        String signer;

        @SerializedName("Signature")
        String signature;
    }
}
