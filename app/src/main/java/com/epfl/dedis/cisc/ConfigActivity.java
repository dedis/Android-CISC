package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.ProposeUpdate;
import com.epfl.dedis.api.ProposeVote;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import static com.epfl.dedis.cisc.ConfigActivity.State.IDLE;
import static com.epfl.dedis.cisc.ConfigActivity.State.POST_VOTE;
import static com.epfl.dedis.cisc.ConfigActivity.State.PRE_VOTE;
import static com.epfl.dedis.cisc.ConfigActivity.State.PROP;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mStatusTextView;

    private Identity mIdentity;

    private State mState;

    public enum State {
        IDLE, PRE_VOTE, POST_VOTE, PROP
    }

    public void taskJoin() {
        String proposal = mIdentity.getProposalString();
        System.out.println(mState + " --- " + proposal);
        if (mState == IDLE) {
            if (proposal == null) {
                mStatusTextView.setText("Skipchain up to date.");
            } else {
                mStatusTextView.setText(proposal);
                mState = PRE_VOTE;
            }
        } else if (mState == PROP && proposal == null) {
            mStatusTextView.setText("Change accepted.");
            mState = IDLE;
        } else if (mState == PRE_VOTE) {
            mStatusTextView.setText("Vote emmited.");
            mState = POST_VOTE;
        } else if (mState == POST_VOTE && proposal == null) {
            mStatusTextView.setText("Threshold reached.");
            mState = IDLE;
        }

        mIdentity.setState(mState);
        SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
        editor.putString(IDENTITY, Utils.toJson(mIdentity));
        editor.apply();
    }

    public void taskFail(int error) {
        if (error == 505) {
            mStatusTextView.setText("Threshold not reached.");
        } else if (error == 503) {
            mStatusTextView.setText("Skipchain up to date.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TextView idTextView = (TextView) findViewById(R.id.config_identity_value);
        TextView addressTextView = (TextView) findViewById(R.id.config_address_value);

        mStatusTextView = (TextView) findViewById(R.id.config_status_value);
        mStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mState == PRE_VOTE) {
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
                if (mState == IDLE) {
                    System.out.println("ADFASFD");
                    new ProposeUpdate(ConfigActivity.this, mIdentity);
                } else if (mState == PROP || mState == POST_VOTE) {
                    new ConfigUpdate(ConfigActivity.this, mIdentity);
                }
            }
        });

//        FloatingActionButton fetchButton = (FloatingActionButton) findViewById(R.id.config_fetch_button);
//        fetchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mProposed) {
//                    new ProposeUpdate(ConfigActivity.this, mIdentity);
//                    mUpdate = false;
//                }
//            }
//        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mIdentity = Utils.fromJson(sharedPreferences.getString(IDENTITY, ""), Identity.class);
        mState = mIdentity.getState();

        idTextView.setText(Utils.encodeBase64(mIdentity.getId()));
        addressTextView.setText(mIdentity.getCothority().getHost());
    }
}