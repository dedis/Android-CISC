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

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements Activity {

    private TextView mIdentityValue;
    private TextView mStatusValue;

    private Identity identity;

    public void callbackSuccess() {
        mIdentityValue.setText(Arrays.toString(identity.getId()));
        mStatusValue.setText(R.string.suc_connection);
    }

    public void callbackError(int error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        mStatusValue.setText(error);
    }

    public void sendConfigUpdate() {
        SharedPreferences pref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String json = pref.getString(IDENTITY, "");
        if (!json.isEmpty()) {
            identity = Utils.fromJson(json, Identity.class);//Identity.load(json);
            new ConfigUpdate(this, identity);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdentityValue = (TextView) findViewById(R.id.main_identity_value);
        assert mIdentityValue != null;

        mStatusValue = (TextView) findViewById(R.id.main_status_value);
        assert mStatusValue != null;

        sendConfigUpdate();

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.main_create_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.main_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mConfigButton = (FloatingActionButton) findViewById(R.id.main_config_button);
        assert mConfigButton != null;
        mConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mRefreshButton = (FloatingActionButton) findViewById(R.id.main_refresh_button);
        assert mRefreshButton != null;
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfigUpdate();
            }
        });
    }
}
