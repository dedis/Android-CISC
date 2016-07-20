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

    private static final String HISTORY = "HISTORY";
    private static final String HOST_KEY = "HOST";
    private static final String PORT_KEY = "PORT";
    private static final String ID_KEY = "ID";

    private boolean checkHistory() {
        SharedPreferences pref = getSharedPreferences(HISTORY, Context.MODE_PRIVATE);
        String host = pref.getString(HOST_KEY, "");
        String port = pref.getString(PORT_KEY, "");
        String id = pref.getString(ID_KEY, "");

        if (host.isEmpty() || port.isEmpty() || id.isEmpty()) {
            mIdentityValue.setText("n/a");
            mStatusValue.setText("n/a");
            mPollValue.setText("n/a");
            return false;
        }
        return true;
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
