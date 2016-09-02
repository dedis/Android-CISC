package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import java.util.Arrays;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mIdTextView;
    private TextView mAddressTextView;

    private SharedPreferences sharedPreferences;

    public void callbackSuccess() {}
    public void callbackError(int error) {}

    private void populate() {
        Identity identity = Utils.fromJson(sharedPreferences.getString(IDENTITY, ""), Identity.class);

        mIdTextView.setText(Arrays.toString(identity.getId()));
        mAddressTextView.setText(identity.getCothority().getHost() + ":" + identity.getCothority().getPort());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        mIdTextView = (TextView) findViewById(R.id.config_identity_value);
        assert mIdTextView != null;

        mAddressTextView = (TextView) findViewById(R.id.config_address_value);
        assert mAddressTextView != null;

        populate();
    }
}
