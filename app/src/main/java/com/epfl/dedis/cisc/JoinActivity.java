package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.ProposeSend;
import com.epfl.dedis.api.ProposeVote;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Identity;

public class JoinActivity extends AppCompatActivity implements Activity {

    private EditText mIdentityEditText;
    private EditText mHostEditText;
    private EditText mPortEditText;

    private int stage;
    private Identity identity;

    private String uuid;

    public void callbackSuccess() {
        if (stage == 0) {
            uuid = Utils.uuid();
            identity.newDevice(uuid);
            new ProposeSend(this, identity);
            stage++;
        } else if (stage == 1) {
            new ProposeVote(this, identity);
            stage++;
        } else {
            ConfigUpdate cu = new ConfigUpdate(this, identity);

            if (cu.getConfig().getDevice().containsKey(uuid)) {

                SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
                editor.putString(IDENTITY, Utils.toJson(identity));
                editor.apply();

                Intent i = new Intent(this, ConfigActivity.class);
                startActivity(i);
                this.finish();
            }
        }
    }

    public void callbackError(int error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
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

        stage = 0;

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.join_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = mHostEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                byte[] id = Utils.fromJson(mIdentityEditText.getText().toString(), byte[].class);
                identity = new Identity(new Cothority(host, port), id);
                new ConfigUpdate(JoinActivity.this, identity);
            }
        });
    }
}
