package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mIdTextView;
    private TextView mAddressTextView;

    private SharedPreferences mSharedPreferences;

    public void taskJoin() {}
    public void taskFail(int error) {}

    private void populate() {
        Identity identity = Utils.fromJson(mSharedPreferences.getString(IDENTITY, ""), Identity.class);

        mIdTextView.setText(Utils.encodeBase64(identity.getId()));
        mAddressTextView.setText(identity.getCothority().getHost() + ":" + identity.getCothority().getPort());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        mIdTextView = (TextView) findViewById(R.id.config_identity_value);
        assert mIdTextView != null;

        mAddressTextView = (TextView) findViewById(R.id.config_address_value);
        assert mAddressTextView != null;

        populate();
    }
}
