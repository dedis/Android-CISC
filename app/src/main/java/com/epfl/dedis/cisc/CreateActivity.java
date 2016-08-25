package com.epfl.dedis.cisc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.net.Replies;

public class CreateActivity extends AppCompatActivity implements Replies {

    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    public void callbackSuccess(String result){
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
        mDataEditText.setText("");
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
                String host = mHostEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                String data = mDataEditText.getText().toString();
//                CreateIdentity ai = new CreateIdentity(CreateActivity.this, host, port, data);
//                ai.sendAddIdentity();
            }
        });
    }
}