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
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    private String host;
    private String port;
    private String data;
    private String id;
    private String publicKey;
    private String privateKey;

    private void clear() {
        mHostEditText.setText("");
        mPortEditText.setText("");
        mDataEditText.setText("");
    }

    private void writePreferences() {
        SharedPreferences.Editor e = getSharedPreferences(Log.LOG.key(), Context.MODE_PRIVATE).edit();
        e.putString(Log.HOST.key(), host);
        e.putString(Log.PORT.key(), port);
        e.putString(Log.DATA.key(), data);
        e.putString(Log.ID.key(), id);
        e.putString(Log.PUBLIC.key(), publicKey);
        e.putString(Log.SECRET.key(), privateKey);
        e.apply();
    }

    private String jsonAddIdentity() {
        Identity identity = new Identity();
        byte[] pub = identity.getPublicKey();
        byte[] sec = identity.getPrivateKey();

        Map<String, byte[]> initDevices = new HashMap<>();
        Map<String, String> initData = new HashMap<>();

        initDevices.put(Build.DEVICE, pub);
        publicKey = Arrays.toString(pub);
        initData.put(Build.DEVICE, data);
        privateKey = Arrays.toString(sec);

        AddIdentity addIdentity = new AddIdentity(0, new Config(3, initDevices, initData));
        return new Gson().toJson(addIdentity);
    }

    private void toast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class AddIdentityThread extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 1000);
                if (socket.isConnected()) {
                    OutputStream outFromServer = socket.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outFromServer);
                    out.writeBytes(jsonAddIdentity());

                    InputStream inFromServer = socket.getInputStream();
                    DataInputStream in = new DataInputStream(inFromServer);

                    byte[] buffer = new byte[1000];
                    int size = in.read(buffer);
                    id = Arrays.toString(buffer).substring(0, size);
                    socket.close();
                    return true;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                toast(R.string.suc_network);
                writePreferences();
                Intent i = new Intent(CreateActivity.this, ConfigActivity.class);
                startActivity(i);
                CreateActivity.this.finish();
            } else {
                toast(R.string.err_network);
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
               clear();
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