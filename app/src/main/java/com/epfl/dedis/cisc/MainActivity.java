package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity implements Activity {

    private TextView mIdentityValue;
    private TextView mStatusValue;

    public void callback(String result) {
        //TODO find proper network error handling
        switch (result) {
            case "1": mStatusValue.setText(R.string.err_not_found); break;
            case "2": mStatusValue.setText(R.string.err_refused); break;
            default:  mStatusValue.setText(R.string.suc_connection);
        }
    }

    private void update() {
        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);
        String host = pref.getString(HOST, "");
        String port = pref.getString(PORT, "");
        String id = pref.getString(ID, "");
        mIdentityValue.setText(id); // TODO Replace TextView with QR-Code
        if (!host.isEmpty() && !port.isEmpty() && !id.isEmpty()) {
            new HTTP(this).execute(host, port, CONFIG_UPDATE, configUpdateJSON(id));
        }
    }

    private String configUpdateJSON(String id) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        int[] idArray = gson.fromJson(id, int[].class);
        return gson.toJson(new ConfigUpdate(idArray, null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdentityValue = (TextView) findViewById(R.id.id_value);
        assert mIdentityValue != null;

        mStatusValue = (TextView) findViewById(R.id.status_value);
        assert mStatusValue != null;

        update();

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.new_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mConfigButton = (FloatingActionButton) findViewById(R.id.configuration_button);
        assert mConfigButton != null;
        mConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mRefreshButton = (FloatingActionButton) findViewById(R.id.refresh_button);
        assert mRefreshButton != null;
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
}
