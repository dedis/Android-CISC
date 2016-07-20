package com.epfl.dedis.cisc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.epfl.dedis.net.Connection;

/**
 * The MainActivity class defines the UI of the
 * start screen and manages to connection to the
 * Cothority server.
 *
 * @author Ignacio Aléman
 * @author Andrea Caforio
 * @since 08.07.2016
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;                  /* Key-value storage */
    private static final String HOST_KEY = "hostname";      /* Hostname key */
    private static final String PORT_KEY = "portnumber";    /* Portnumber key */

    /**
     * Display common toast marker on the screen.
     *
     * @param text resource id of the string to be displayed
     */
    public void toast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mCreateButton = (Button) findViewById(R.id.create_button);
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
