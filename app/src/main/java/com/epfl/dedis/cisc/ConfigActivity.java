package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.ProposeUpdate;
import com.epfl.dedis.api.ProposeVote;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

import java.util.Arrays;
import java.util.HashSet;
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
            if (identity.getProposed() == null) {
                mStatusTextView.setText(R.string.info_uptodate);
            } else {
                Set<Map.Entry<String, String>> config = new HashSet<>(identity.getConfig().getDevice().entrySet());
                Set<Map.Entry<String, String>> cData = new HashSet<>(identity.getConfig().getData().entrySet());

                Set<Map.Entry<String, String>> proposed = new HashSet<>(identity.getProposed().getDevice().entrySet());
                Set<Map.Entry<String, String>> pData = new HashSet<>(identity.getProposed().getData().entrySet());

                if (config.equals(proposed) && cData.equals(pData)) {
                    mStatusTextView.setText(R.string.info_acceptedchange);
                    identity.setProposed(null);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(IDENTITY, Utils.toJson(identity));
                    editor.apply();
                }
            }
        } else {
            Set<Map.Entry<String, String>> config = new HashSet<>(identity.getConfig().getDevice().entrySet());
            Set<Map.Entry<String, String>> proposed = new HashSet<>(identity.getProposed().getDevice().entrySet());

            Set<Map.Entry<String, String>> cData = new HashSet<>(identity.getConfig().getData().entrySet());
            Set<Map.Entry<String, String>> pData = new HashSet<>(identity.getProposed().getData().entrySet());

            proposed.removeAll(config);
            pData.removeAll(cData);
            if (proposed.size() != 0) {
                mStatusTextView.setText(Arrays.toString(proposed.toArray()));
            } else {
                mStatusTextView.setText(Arrays.toString(pData.toArray()));
            }
        }
    }

    public void taskFail(int error) {
        mStatusTextView.setText(error);
    }

    private void populate() {
        identity = Utils.fromJson(mSharedPreferences.getString(IDENTITY, ""), Identity.class);

        mIdTextView.setText(Utils.encodeBase64(identity.getId()));
        String address = identity.getCothority().getHost() + ":" + identity.getCothority().getPort();
        mAddressTextView.setText(address);
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
        mStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProposeVote(ConfigActivity.this, identity);
                Toast.makeText(ConfigActivity.this, "Voted", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        mStatusTextView.setText(intent.getStringExtra(STATUS_INTENT));

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

        FloatingActionButton fetchButton = (FloatingActionButton) findViewById(R.id.config_fetch_button);
        assert fetchButton != null;
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProposeUpdate(ConfigActivity.this, identity);
                update = false;
            }
        });
    }
}
