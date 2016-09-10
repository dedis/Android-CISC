package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.api.ProposeSend;
import com.epfl.dedis.api.ProposeVote;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class DataActivity extends AppCompatActivity implements Activity {

    private TextView mNewTextView;

    private Identity mIdentity;
    private boolean mProposed;

    public void taskJoin() {
        if (!mProposed) {
            new ProposeVote(this, mIdentity);
            mProposed = true;
        } else {
            startActivity(new Intent(this, ConfigActivity.class));
            finish();
        }
    }

    public void taskFail(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void generateRSAKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

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
    }
}
