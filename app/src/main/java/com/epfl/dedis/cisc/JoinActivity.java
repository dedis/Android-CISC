package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JoinActivity extends AppCompatActivity implements Message {

    private EditText mIdentityEditText;
    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    //TODO are those class fields necessary? (See MainActivity for alternative)
    private String host;
    private String port;
    private String data;
    private String id;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    private Gson gson;
    private int stage;

    // TODO Proper state machine for joining process
    // TODO Find nice error messages
    public void callback(String result) {
        switch (result) {
            case "1": toast(R.string.err_refused); break;
            case "2": toast(R.string.err_add_identity); break;
            default: {
                if (stage == 0) {
                    sendProposeSend(result);
                } else if (stage == 1) {
                    sendProposeVote(result);
                } else {

                }
            }
        }
    }

    private void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private String configUpdateJSON() {
        int[] idArray = gson.fromJson(id, int[].class);
//        ConfigUpdate cu = new ConfigUpdate(idArray, null);
//        return gson.toJson(cu);
        return null;
    }

    private String proposeSendJSON(String json) {
        ConfigUpdate configUpdate = gson.fromJson(json, ConfigUpdate.class);
        Ed25519 curve = new Ed25519();
//        int[] pub = curve.getPublic();
//
//        configUpdate.getAccountList().getDevice().put(DEVICE, pub);
//        configUpdate.getAccountList().getData().put(DEVICE, DEVICE);
        return gson.toJson(configUpdate);
    }

    public void sendConfigUpdate() {
        if (host.isEmpty() || port.isEmpty() || data.isEmpty() || id.isEmpty()) {
            toast(R.string.err_empty_fields);
        } else {
            stage++;
//            new HTTP(JoinActivity.this).execute(host, port, CONFIG_UPDATE, configUpdateJSON());
        }
    }

    public void sendProposeSend(String result) {
        SharedPreferences.Editor editor = getSharedPreferences(LOG, Context.MODE_PRIVATE).edit();
        editor.putString("LATEST", result);
        editor.apply();
        stage++;
//        new HTTP(this).execute(host, port, PROPOSE_SEND, proposeSendJSON(result));
    }

    public void sendProposeVote(String result) {
        SharedPreferences.Editor editor = getSharedPreferences(LOG, Context.MODE_PRIVATE).edit();
        editor.putString("PROPOSED", result);
        editor.apply();
        stage++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mIdentityEditText = (EditText) findViewById(R.id.join_identity_edit);
        assert mIdentityEditText != null;

        mHostEditText = (EditText) findViewById(R.id.join_host_edit);
        assert mHostEditText != null;

        mPortEditText = (EditText) findViewById(R.id.join_port_edit);
        assert mPortEditText != null;

        mDataEditText = (EditText) findViewById(R.id.join_data_edit);
        assert mDataEditText != null;

        gson = new GsonBuilder().serializeNulls().create();
        stage = -1;

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.join_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host = mHostEditText.getText().toString();
                port = mPortEditText.getText().toString();
                data = mDataEditText.getText().toString();
                id = mIdentityEditText.getText().toString();
                sendConfigUpdate();
            }
        });
    }
}
