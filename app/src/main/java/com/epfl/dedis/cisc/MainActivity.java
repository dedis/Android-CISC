package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Activity {

    private TextView mIdentityValue;
    private TextView mStatusValue;

    private String host;
    private String port;
    private String id;

    private boolean checkLog() {
        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);
        host = pref.getString(HOST, "");
        port = pref.getString(PORT, "");
        id = pref.getString(ID, "n/a");
        mIdentityValue.setText(id);
        return host.isEmpty() || port.isEmpty();
    }

    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void update() {
        if (!checkLog()) {
            new ConfigUpdateThread().execute();
        }
    }

    private class ConfigUpdateThread extends AsyncTask<Void, Void, Boolean> implements Thread {

        public String makeJson() {
            Gson gson = new GsonBuilder().serializeNulls().create();
            int[] idArray = gson.fromJson(id, int[].class);
            ConfigUpdate cu = new ConfigUpdate(idArray, null);
            return gson.toJson(cu);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HTTP.open(host, port, CONFIG_UPDATE, makeJson());
                return true;
            } catch(IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mStatusValue.setText(getString(R.string.status_online_value));
                mStatusValue.setTextColor(Color.GREEN);
            } else {
                mStatusValue.setText(getString(R.string.status_offine_value));
                mStatusValue.setTextColor(Color.RED);
            }
        }
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

        Button mCreateButton = (Button) findViewById(R.id.new_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });

        Button mRetryButton = (Button) findViewById(R.id.retry_button);
        assert mRetryButton != null;
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        Button mConfigButton = (Button) findViewById(R.id.configuration_button);
        assert mConfigButton != null;
        mConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(i);
            }
        });
    }
}
