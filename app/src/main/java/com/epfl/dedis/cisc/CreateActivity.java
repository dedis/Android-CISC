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
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Cothority;

public class CreateActivity extends AppCompatActivity implements Activity {

    private EditText mHostEditText;
    private EditText mPortEditText;

    private CreateIdentity mCreateIdentity;

    public void taskJoin() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
        editor.putString(IDENTITY, Utils.toJson(mCreateIdentity.getIdentity()));
        editor.apply();

        startActivity(new Intent(this, ConfigActivity.class));
        finish();
    }

    public void taskFail(int error){
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
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clearFields();
            }
        });

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.create_create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = mHostEditText.getText().toString();
                String port = mPortEditText.getText().toString();

                if (host.isEmpty() || port.isEmpty()) {
                    taskFail(R.string.err_empty);
                } else {
                    mCreateIdentity = new CreateIdentity(CreateActivity.this, Utils.uuid(), new Cothority(host, port));
                }
            }
        });
    }
}