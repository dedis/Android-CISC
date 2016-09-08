package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import java.util.Map;
import java.util.Set;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mIdTextView;
    private TextView mAddressTextView;
    private TextView mStatusTextView;

    private SharedPreferences mSharedPreferences;
    private Identity identity;

    private boolean update;

    public void taskJoin() {
        if (update) {
            Set<Map.Entry<String, String>> config = identity.getConfig().getDevice().entrySet();
            Set<Map.Entry<String, String>> cData = identity.getConfig().getData().entrySet();

            Set<Map.Entry<String, String>> proposed = identity.getProposed().getDevice().entrySet();
            Set<Map.Entry<String, String>> pData = identity.getProposed().getData().entrySet();

            System.out.println(Utils.toJson(identity.getConfig()));
            System.out.println(Utils.toJson(identity.getProposed()));

            if (config.equals(proposed) && cData.equals(pData)) {
                mStatusTextView.setText("Skipchain up to date");
                identity.setProposed(null);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(IDENTITY, Utils.toJson(identity));
                editor.apply();
            }
        } else {
            System.out.println("");
        }
    }
    public void taskFail(int error) {}

    private void populate() {
        identity = Utils.fromJson(mSharedPreferences.getString(IDENTITY, ""), Identity.class);

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

        mStatusTextView = (TextView) findViewById(R.id.config_status_value);
        assert mStatusTextView != null;

        Intent intent = getIntent();
        mStatusTextView.setText(intent.getStringExtra("STATUS"));

        populate();

        FloatingActionButton deviceButton = (FloatingActionButton) findViewById(R.id.config_devices_button);
        assert deviceButton != null;
        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, DeviceActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton refreshButton = (FloatingActionButton) findViewById(R.id.config_refresh_button);
        assert refreshButton != null;
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfigUpdate(ConfigActivity.this, identity);
                update = true;
            }
        });
    }
}
