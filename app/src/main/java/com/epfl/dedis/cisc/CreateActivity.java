package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.api.CreateIdentity;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Replies;

public class CreateActivity extends AppCompatActivity implements Replies {

    private EditText mHostEditText;
    private EditText mPortEditText;

    private void writePreference(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void callbackSuccess(String result) {
        writePreference(ID, result);
        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
        this.finish(); //TODO choose correct Replies termination
    }

    public void callbackError(int error){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void clearFields() {
        mHostEditText.setText("");
        mPortEditText.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mHostEditText = (EditText) findViewById(R.id.create_host_edit);
        assert mHostEditText != null;

        mPortEditText = (EditText) findViewById(R.id.create_port_edit);
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
                String host = mHostEditText.getText().toString();
                String port = mPortEditText.getText().toString();

                if (host.isEmpty() || port.isEmpty()) {
                    callbackError(R.string.err_empty_fields);
                } else {
                    writePreference(HOST, host);
                    writePreference(PORT, port);
                    new CreateIdentity(CreateActivity.this, new Cothority(host, port));
                }
            }
        });
    }
}