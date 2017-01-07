package ch.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.dedis.api.ProposeSend;
import ch.epfl.dedis.api.ProposeVote;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Identity;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static ch.epfl.dedis.cisc.DataActivity.DataState.PROP;
import static ch.epfl.dedis.cisc.DataActivity.DataState.VOTE;

/**
 * The DataActivity is used to add or update data for the device.
 * After sending a successful proposal the Activity also performs
 * the vote on it before switching back to the ConfigActivity.
 *
 * @author Andrea Caforio
 */
public class DataActivity extends AppCompatActivity implements Activity {

    private static final String TAG = "cisc.DataActivity";

    private TextView mNewTextView;

    private Identity mIdentity;
    private DataState mDataState;

    /**
     * State transitions according to operation that has been
     * performed.
     */
    public void taskJoin() {
        Log.d(TAG, "Task join: " + mDataState.name());
        if (mDataState == PROP) {
            new ProposeVote(this, mIdentity);
            mDataState = VOTE;
        } else if (mDataState == VOTE){
            // After voting set ConfigState to PROP for ConfigActivity.
            mIdentity.setConfigState(ConfigActivity.ConfigState.PROP);
            SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
            editor.putString(IDENTITY, Utils.toJson(mIdentity));
            editor.apply();

            Toast.makeText(this, R.string.info_proposalsent, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(new Intent(this, ConfigActivity.class));
            startActivity(intent);
            finish();
        }
    }

    public void taskFail(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Update or add new RSA key pair to the Identity.
     */
    public void generateRSAKeyPair() {
        try {
            // Only 256-bit keys supported for stable efficiency.
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(256);

            String pub = Utils.encodeBase64(keyGen.genKeyPair().getPublic().getEncoded());
            mIdentity.updateData(pub);
            mIdentity.setRSASecret(keyGen.genKeyPair().getPublic().getEncoded());
            mNewTextView.setText(pub);
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Log.d(TAG, "onCreate called.");

        SharedPreferences sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mIdentity = Utils.fromJson(sharedPreferences.getString(IDENTITY, ""), Identity.class);

        TextView oldTextView = (TextView) findViewById(R.id.data_old_value);
        String data = mIdentity.getConfig().getData().get(mIdentity.getName());
        if (data == null) {
            oldTextView.setText(R.string.info_nodata);
        } else {
            oldTextView.setText(data);
        }

        mNewTextView = (TextView) findViewById(R.id.data_new_value);
        generateRSAKeyPair();

        FloatingActionButton newButton = (FloatingActionButton) findViewById(R.id.data_new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRSAKeyPair();
            }
        });

        FloatingActionButton proposeButton = (FloatingActionButton) findViewById(R.id.data_propose_button);
        proposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProposeSend(DataActivity.this, mIdentity);
            }
        });

        mDataState = PROP;
    }

    /**
     * Keywords for the marking the current state of the DataActivity.
     *
     * PROP:    State after initializing the Activity.
     * VOTE:    Reached after emitting a ProposeSend.
     */
    public enum DataState {
        PROP, VOTE
    }
}
