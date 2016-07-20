package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mIdentityValue;
    private TextView mStatusValue;
    private TextView mPollValue;

    private boolean checkHistory() {
        SharedPreferences pref = getSharedPreferences(Log.LOG.key(), Context.MODE_PRIVATE);
        String host = pref.getString(Log.HOST.key(), "");
        String port = pref.getString(Log.PORT.key(), "");
        String id = pref.getString(Log.ID.key(), "");

        mIdentityValue.setText(host);
        mStatusValue.setText(port);
        mPollValue.setText(id);
        return host.isEmpty() || port.isEmpty() || id.isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdentityValue = (TextView) findViewById(R.id.id_value);
        assert mIdentityValue != null;

        mStatusValue = (TextView) findViewById(R.id.status_value);
        assert mStatusValue != null;

        mPollValue = (TextView) findViewById(R.id.poll_value);
        assert mPollValue != null;

        if (checkHistory()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }

        Button mCreateButton = (Button) findViewById(R.id.new_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });
    }
}
