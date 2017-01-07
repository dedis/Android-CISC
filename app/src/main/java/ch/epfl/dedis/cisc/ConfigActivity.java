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

import ch.epfl.dedis.api.ConfigUpdate;
import ch.epfl.dedis.api.ProposeUpdate;
import ch.epfl.dedis.api.ProposeVote;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Identity;

import static ch.epfl.dedis.cisc.ConfigActivity.ConfigState.IDLE;
import static ch.epfl.dedis.cisc.ConfigActivity.ConfigState.POST_VOTE;
import static ch.epfl.dedis.cisc.ConfigActivity.ConfigState.PRE_VOTE;
import static ch.epfl.dedis.cisc.ConfigActivity.ConfigState.PROP;

/**
 * This Activity marks the core of the application. Here a user manages
 * his data, votes on proposed configurations and checks the current state
 * of an Identity.
 *
 * @author Andrea Caforio
 */
public class ConfigActivity extends AppCompatActivity implements Activity {

    private static final String TAG = "cisc.ConfigActivity";

    private TextView mStatusTextView;

    private Identity mIdentity;
    private ConfigState mConfigState;

    /**
     * State transitions according to the current operation that
     * has been performed.
     */
    public void taskJoin() {
        Log.d(TAG, "Task join: " + mConfigState.name());
        String proposal = mIdentity.getProposalString();
        if (mConfigState == IDLE) {
            if (proposal == null) {
                mStatusTextView.setText(R.string.info_uptodate);
            } else {
                mStatusTextView.setText(proposal);
                mConfigState = PRE_VOTE;
            }
        } else if (mConfigState == PROP && proposal == null) {
            mStatusTextView.setText(R.string.info_acceptedchange);
            mConfigState = IDLE;
        } else if (mConfigState == PRE_VOTE) {
            mStatusTextView.setText(R.string.info_voted);
            mConfigState = POST_VOTE;
        } else if (mConfigState == POST_VOTE) {
            if (proposal == null) {
                mStatusTextView.setText(R.string.info_thresholdreached);
                mConfigState = IDLE;
            } else {
                mStatusTextView.setText(R.string.info_thresholdnotreached);
            }
        }

        // Store new state in Identity and shared preferences.
        mIdentity.setConfigState(mConfigState);
        SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
        editor.putString(IDENTITY, Utils.toJson(mIdentity));
        editor.apply();
    }

    public void taskFail(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Log.d(TAG, "onCreate called.");

        TextView idTextView = (TextView) findViewById(R.id.config_identity_value);
        TextView addressTextView = (TextView) findViewById(R.id.config_address_value);

        mStatusTextView = (TextView) findViewById(R.id.config_status_value);
        mStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only vote when proposal is pending.
                if (mConfigState == PRE_VOTE) {
                    new ProposeVote(ConfigActivity.this, mIdentity);
                }
            }
        });

        FloatingActionButton changeButton = (FloatingActionButton) findViewById(R.id.config_change_button);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, DataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton deviceButton = (FloatingActionButton) findViewById(R.id.config_devices_button);
        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, DeviceActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton refreshButton = (FloatingActionButton) findViewById(R.id.config_refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConfigState == IDLE) {
                    new ProposeUpdate(ConfigActivity.this, mIdentity);
                } else if (mConfigState == PROP || mConfigState == POST_VOTE) {
                    new ConfigUpdate(ConfigActivity.this, mIdentity);
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mIdentity = Utils.fromJson(sharedPreferences.getString(IDENTITY, ""), Identity.class);
        mConfigState = mIdentity.getConfigState();

        // Set correct state message fetched from Identity block.
        switch (mConfigState) {
            case IDLE: mStatusTextView.setText(R.string.info_uptodate);
                break;
            case PRE_VOTE: mStatusTextView.setText(mIdentity.getProposalString());
                break;
            case PROP: mStatusTextView.setText(R.string.info_thresholdnotreached);
                break;
            case POST_VOTE: mStatusTextView.setText(R.string.info_voted);
                break;
            default: throw new AssertionError("Unknown state.");
        }

        idTextView.setText(Utils.encodeBase64(mIdentity.getId()));
        addressTextView.setText(mIdentity.getCothority().getHost());
    }

    /**
     * Keywords for the marking the current state of the ConfigActivity.
     *
     * IDLE:        Skipchain is up to date, no pending proposals.
     * PRE_VOTE:    Proposal is detected in the IDLE state.
     * POST_VOTE:   After voting on a proposal in the PRE_VOTE state.
     * PROP:        Used from Join- and DataActivity after a new proposal.
     */
    public enum ConfigState {
        IDLE, PRE_VOTE, POST_VOTE, PROP
    }
}