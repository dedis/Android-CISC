package com.epfl.dedis.cisc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TextView tv = (TextView) findViewById(R.id.testView);
        tv.setText(getSharedPreferences("HISTORY", Context.MODE_PRIVATE).getString("ID", "NOT FOUND"));
    }
}
