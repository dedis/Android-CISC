package com.epfl.dedis.cisc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JoinActivity extends AppCompatActivity implements Activity {

    private EditText mIdentityEditText;
    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    //TODO are those class fields necessary? (See MainActivity for alternative)
    private String host;
    private String port;
    private String data;
    private String id;

    private Gson gson;
    private int stage;

    protected String debug;

    // TODO Proper state machine for joining process
    public void callback(String result) {
        if (stage == 0) {
            debug = result;
            new HTTP(this).execute(host, port, PROPOSE_SEND, proposeSendJSON(result));
        } else if (stage == 1){
            debug = result;
        } else {

        }
        stage++;
    }

    private void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private String configUpdateJSON() {
        int[] idArray = gson.fromJson(id, int[].class);
        ConfigUpdate cu = new ConfigUpdate(idArray, null);
        return gson.toJson(cu);
    }

    private String proposeSendJSON(String json) {
        ConfigUpdate configUpdate = gson.fromJson(json, ConfigUpdate.class);
        Ed25519 curve = new Ed25519();
        int[] pub = curve.getPublic();

        configUpdate.getAccountList().getDevice().put(DEVICE, pub);
        configUpdate.getAccountList().getData().put(DEVICE, DEVICE);
        return gson.toJson(configUpdate);
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
        stage = 0;

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.join_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host = mHostEditText.getText().toString();
                port = mPortEditText.getText().toString();
                data = mDataEditText.getText().toString();
                id = mIdentityEditText.getText().toString();

                if (host.isEmpty() || port.isEmpty() || data.isEmpty() || id.isEmpty()) {
                    toast(R.string.err_empty_fields);
                } else {
                    new HTTP(JoinActivity.this).execute(host, port, CONFIG_UPDATE, configUpdateJSON());
                }
            }
        });
    }
}
