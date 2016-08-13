package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity implements Activity {

    private TextView mIdentityValue;
    private TextView mStatusValue;

    private String host;
    private String port;
    private String id;

    public void callback(String result) {
        switch (result) {
            case "1": mStatusValue.setText(R.string.err_not_found); break;
            case "2": mStatusValue.setText(R.string.err_refused); break;
            default:  mStatusValue.setText(R.string.suc_connection);
        }
    }

//    private boolean checkLog() {
//        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);
//        host = pref.getString(HOST, "");
//        port = pref.getString(PORT, "");
//        id = pref.getString(ID, "");
//        mIdentityValue.setText(id);
//        return host.isEmpty() || port.isEmpty();
//    }

    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void update() {
        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);
        host = pref.getString(HOST, "");
        port = pref.getString(PORT, "");
        id = pref.getString(ID, "");
        mIdentityValue.setText(id);
        if (!host.isEmpty() && !port.isEmpty()) {
            Log.i(getClass().getName(), makeJson());
            new HTTP(this).execute(host, port, CONFIG_UPDATE, makeJson());
        }
    }

    // For debugging purposes
    public void setId(String id) {
        this.id = id;
    }

    public String makeJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        int[] idArray = gson.fromJson(id, int[].class);
        ConfigUpdate cu = new ConfigUpdate(idArray, null);
        return gson.toJson(cu);
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
