package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.crypto.Identity;
import com.epfl.dedis.net.AddIdentity;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity implements Activity {

    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    private String host;
    private String port;
    private String data;
    private String id;
    private String publicKey;
    private String privateKey;

    private void clearFields() {
        mHostEditText.setText("");
        mPortEditText.setText("");
        mDataEditText.setText("");
    }

    private void writeLog() {
        SharedPreferences.Editor e = getSharedPreferences(LOG, Context.MODE_PRIVATE).edit();
        e.putString(HOST, host);
        e.putString(PORT, port);
        e.putString(DATA, data);
        e.putString(ID, id);
        e.putString(PUBLIC, publicKey);
        e.putString(SECRET, privateKey);
        e.apply();
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private class AddIdentityThread extends AsyncTask<Void, Void, String> implements Thread {

        public String makeJson() {
            Identity identity = new Identity();
            byte[] pub = identity.getPublic();
            byte[] sec = identity.getPrivate();

            Map<String, byte[]> initDevices = new HashMap<>();
            Map<String, String> initData = new HashMap<>();

            initDevices.put(Build.DEVICE, pub);
            publicKey = Arrays.toString(pub);
            initData.put(Build.DEVICE, data);
            privateKey = Arrays.toString(sec);

            AddIdentity addIdentity = new AddIdentity(new Config(3, initDevices, initData));
            return new Gson().toJson(addIdentity);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String ack = HTTP.open(host, port, ADD_IDENTITY, makeJson());
                id = ack;
                return "";
            } catch(IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String error) {
            if (error.isEmpty()) {
                writeLog();
                Intent i = new Intent(CreateActivity.this, ConfigActivity.class);
                startActivity(i);
                CreateActivity.this.finish();
            } else {
                toast(error);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mHostEditText = (EditText) findViewById(R.id.host_editText);
        assert mHostEditText != null;

        mPortEditText = (EditText) findViewById(R.id.port_editText);
        assert mPortEditText != null;

        mDataEditText = (EditText) findViewById(R.id.data_editText);
        assert mPortEditText != null;

        Button mClearButton = (Button) findViewById(R.id.clear_button);
        assert mClearButton != null;
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clearFields();
            }
        });

        Button mCreateButton = (Button) findViewById(R.id.create_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host = mHostEditText.getText().toString();
                port = mPortEditText.getText().toString();
                data = mDataEditText.getText().toString();

                if (host.isEmpty() || port.isEmpty() || data.isEmpty()) {
                    toast(R.string.err_empty_fields);
                } else {
                    new AddIdentityThread().execute();
                }
            }
        });
    }
}