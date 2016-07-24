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
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Activity {

    private TextView mIdentityValue;
    private TextView mStatusValue;
    private TextView mPollValue;

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

    private class ConfigUpdateThread extends AsyncTask<Void, Void, Boolean> implements Thread {

        public String makeJson() {
            Gson gson = new GsonBuilder().serializeNulls().create();
            ConfigUpdate cu = new ConfigUpdate(id, null);
            return gson.toJson(cu);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HTTP.open(host, port, "cu", makeJson());
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
                mPollValue.setText(new Date().toString());
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

        mPollValue = (TextView) findViewById(R.id.poll_value);
        assert mPollValue != null;

        if (!checkLog()) {
            new ConfigUpdateThread().execute();
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }

        Button mCreateButton = (Button) findViewById(R.id.new_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });
    }
}
