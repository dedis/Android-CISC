package ch.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import ch.epfl.dedis.api.ConfigUpdate;
import ch.epfl.dedis.api.ProposeSend;
import ch.epfl.dedis.crypto.QRStamp;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Cothority;
import ch.epfl.dedis.net.Identity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static ch.epfl.dedis.cisc.JoinActivity.JoinState.CONF;
import static ch.epfl.dedis.cisc.JoinActivity.JoinState.PROP;

/**
 * The JoinActivity deploys the QR-scanner used to register the Identity hash,
 * the host address and port to be able to join an existing Skipchain.
 * A join procedure does not require a vote unlike updating the Config.
 * After a successful proposition the ConfigActivity is started.
 *
 * @author Andrea Caforio
 */
public class JoinActivity extends AppCompatActivity implements Activity, ZXingScannerView.ResultHandler {

    private static final String TAG = "cisc.JoinActivity";

    private ZXingScannerView mScannerView;
    private Identity mIdentity;
    private JoinState mJoinState;

    /**
     * State transitions corresponding to operation that has been
     * performed.
     */
    public void taskJoin() {
        Log.d(TAG, "Task join: " + mJoinState.name());
        if (mJoinState == CONF) {
            mIdentity.newDevice(Utils.uuid());
            new ProposeSend(this, mIdentity);
            mJoinState = PROP;
        } else if (mJoinState == PROP) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
            editor.putString(IDENTITY, Utils.toJson(mIdentity));
            editor.apply();

            Intent intent = new Intent(JoinActivity.this, ConfigActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void taskFail(int error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called.");
        mScannerView.stopCamera();
    }

    /**
     * Parsing the raw data from the QR-scanning into a Identity
     * object.
     *
     * @param rawResult from scanner
     */
    @Override
    public void handleResult(Result rawResult) {
        String json = rawResult.getText();
        Log.d(TAG, "Obtained result from scanner: " + json);
        QRStamp qrs = Utils.fromJson(json, QRStamp.class);
        Cothority cothority = new Cothority(qrs.getHost(), qrs.getPort());

        // Set PROP state in identity for ConfigActivity.
        mIdentity = new Identity(cothority, Utils.decodeBase64(qrs.getId()), ConfigActivity.ConfigState.PROP);
        new ConfigUpdate(JoinActivity.this, mIdentity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateCalled.");

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        mJoinState = CONF;
    }

    /**
     * Keywords for the marking the current state of the JoinActivity.
     *
     * CONF:    Initial state after initialization.
     * PROP:    Entered after sending the ProposeSend.
     */
    public enum JoinState {
        CONF, PROP
    }
}