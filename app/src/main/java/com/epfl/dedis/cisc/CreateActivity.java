package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.net.AddIdentity;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private String publicKey;
    private String privateKey;

    public void callback(String result) {
        switch (result) {
            case "1": toast(R.string.err_add_identity); break;
            case "2": toast(R.string.err_refused); break;
            default: {
                writeLog(result);
                Intent i = new Intent(this, ConfigActivity.class);
                startActivity(i);
                this.finish(); //TODO choose correct Activity termination
            }
        }
    }

    private void clearFields() {
        mHostEditText.setText("");
        mPortEditText.setText("");
        mDataEditText.setText("");
    }

    private void writeLog(String id) {
        SharedPreferences.Editor e = getSharedPreferences(LOG, Context.MODE_PRIVATE).edit();
        e.putString(HOST, host);
        e.putString(PORT, port);
        e.putString(DATA, data);
        e.putString(ID, id);
        e.putString(PUBLIC, publicKey);
        e.putString(SECRET, privateKey);
        e.apply();
    }

    private void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public String makeJson() {
        Ed25519 curve = new Ed25519();
        byte[] pub = curve.getPublic();
        byte[] sec = curve.getPrivate();

        Map<String, byte[]> initDevices = new HashMap<>();
        Map<String, String> initData = new HashMap<>();

        initDevices.put(DEVICE, pub);
        publicKey = Arrays.toString(pub);
        initData.put(DEVICE, data);
        privateKey = Arrays.toString(sec);

        AddIdentity addIdentity = new AddIdentity(new Config(3, initDevices, initData));
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(addIdentity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mHostEditText = (EditText) findViewById(R.id.create_host_edit);
        assert mHostEditText != null;

        mPortEditText = (EditText) findViewById(R.id.create_port_edit);
        assert mPortEditText != null;

        mDataEditText = (EditText) findViewById(R.id.create_data_edit);
        assert mPortEditText != null;

        FloatingActionButton mClearButton = (FloatingActionButton) findViewById(R.id.create_clear_button);
        assert mClearButton != null;
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clearFields();
            }
        });

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.create_create_button);
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
                    new HTTP(CreateActivity.this).execute(host, port, ADD_IDENTITY, makeJson());
                }
            }
        });
    }
}