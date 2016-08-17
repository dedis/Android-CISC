package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String id;
    private String publicKey;
    private String privateKey;

    private Ed25519 curve;

    public void callback(String result) {
        switch (result) {
            case "1": toast(R.string.err_add_identity); break;
            case "2": toast(R.string.err_refused); break;
            default: {
                id = result;
                writeLog();
                Intent i = new Intent(this, ConfigActivity.class);
                startActivity(i);
                this.finish();
            }
        }
    }

    private void clearFields() {
        mHostEditText.setText("");
        mPortEditText.setText("");
        mDataEditText.setText("");
    }

    public void writeLog() {
        SharedPreferences.Editor e = getSharedPreferences(LOG, Context.MODE_PRIVATE).edit();
        e.putString(HOST, host);
        e.putString(PORT, port);
        e.putString(DATA, data);
        e.putString(ID, id);
        e.putString(PUBLIC, publicKey);
        e.putString(SECRET, privateKey);
        e.apply();
    }

    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public String makeJson() {
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
        curve = new Ed25519();

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
                    System.out.println("-------> " + data);
                    toast(R.string.err_empty_fields);
                } else {

                    new HTTP(CreateActivity.this).execute(host, port, ADD_IDENTITY, makeJson());
                }
            }
        });
    }
}