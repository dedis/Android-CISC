package com.epfl.dedis.cisc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JoinActivity extends AppCompatActivity implements Activity {

    private EditText mIdentityEditText;
    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    //TODO are those class fields necessary? (See MainActivity for alternative)
    private String host;
    private String port;
    private String data;
    private String id;

    public void callback(String result) {
        System.out.println(result);
    }

    private void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private String configUpdateJSON() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        int[] idArray = gson.fromJson(id, int[].class);
        ConfigUpdate cu = new ConfigUpdate(idArray, null);
        return gson.toJson(cu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mIdentityEditText = (EditText) findViewById(R.id.id_editText);
        assert mIdentityEditText != null;

        mHostEditText = (EditText) findViewById(R.id.host_editText);
        assert mHostEditText != null;

        mPortEditText = (EditText) findViewById(R.id.port_editText);
        assert mPortEditText != null;

        mDataEditText = (EditText) findViewById(R.id.data_editText);
        assert mDataEditText != null;

        Button mJoinButton = (Button) findViewById(R.id.join_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host = mHostEditText.getText().toString();
                port = mPortEditText.getText().toString();
                data = mDataEditText.getText().toString();
                id = mIdentityEditText.getText().toString();

                if (host.isEmpty() || port.isEmpty() || data.isEmpty() || id.isEmpty()) {
                    toast(R.string.err_empty_fields);
                } else {
                    new HTTP(JoinActivity.this).execute(host, port, CONFIG_UPDATE, configUpdateJSON());
                }
            }
        });
    }
}
