package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity implements Activity {

    private TextView mIdTextView;
    private TextView mAddressTextView;
    private TextView mDateTextView;
    private TextView mDataTextView;

    private void populate() {
        SharedPreferences pref = getSharedPreferences(LOG, Context.MODE_PRIVATE);

        mIdTextView.setText(pref.getString(ID, ""));

        String address = pref.getString(HOST, "") + ":" + pref.getString(PORT, "");
        mAddressTextView.setText(address);

        mDataTextView.setText(pref.getString(DATA, ""));
    }

    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mIdTextView = (TextView) findViewById(R.id.config_id_view);
        assert mIdTextView != null;

        mAddressTextView = (TextView) findViewById(R.id.config_address_view);
        assert mAddressTextView != null;

        mDateTextView = (TextView) findViewById(R.id.config_date_view);
        assert mDateTextView != null;

        mDataTextView = (TextView) findViewById(R.id.config_data_view);
        assert mDataTextView != null;

        populate();
    }
}
