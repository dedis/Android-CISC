package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mIdTextView;
    private TextView mAddressTextView;
    private TextView mDataTextView;

    // TODO for later when voting is being implemented
    public void callback(String result) {}

    private void populate() {
        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);
        mIdTextView.setText(pref.getString(ID, ""));

        String address = pref.getString(HOST, "") + ":" + pref.getString(PORT, "");
        mAddressTextView.setText(address);

        mDataTextView.setText(pref.getString(DATA, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mIdTextView = (TextView) findViewById(R.id.config_identity_value);
        assert mIdTextView != null;

        mAddressTextView = (TextView) findViewById(R.id.config_address_value);
        assert mAddressTextView != null;

        mDataTextView = (TextView) findViewById(R.id.config_data_value);
        assert mDataTextView != null;

        populate();
    }
}
