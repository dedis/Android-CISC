package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    private EditText mHostEditText; /* EditText field for hostname */
    private EditText mPortEditText; /* EditText field for portname */
    private EditText mIdEditText;   /* EditText field for identity */


    private InputMethodManager imm; /* IMM to handle window peripherals */

    private Connection connection;   /* Connection facility */

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

    /**
     * Called at startup to fetch a possibly already existing
     * preferences in the key-value storage.
     */
    private void checkHistory() {
        preferences = getPreferences(Context.MODE_PRIVATE);
        mHostEditText.setText(preferences.getString(HOST_KEY, ""));
        mPortEditText.setText(preferences.getString(PORT_KEY, ""));
    }

    /**
     * Whenever a connection is requested, the entered information
     * in the fields is stored in the internal key-value storage.
     */
    public void writeHistory() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HOST_KEY, mHostEditText.getText().toString());
        editor.putString(PORT_KEY, mPortEditText.getText().toString());
        editor.apply();
    }

    private boolean isBlank(EditText field) {
        return field.getText().toString().isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHostEditText = (EditText) findViewById(R.id.host_editText);
        mHostEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            /**
             * Clear cursor and hide keyboard when input is terminated.
             *
             * @param v unused
             * @param actionId identifier of the keyboard action
             * @param event unused
             * @return <code>true</code> if done key was pressed
             *         <code>false</code> otherwise
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mHostEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mHostEditText.getWindowToken(), 0);
                }
                return false;
            }
        });


        mPortEditText = (EditText) findViewById(R.id.port_editText);
        mPortEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            /**
             * Clear cursor and hide keyboard when input is terminated.
             *
             * @param v unused
             * @param actionId identifier of the keyboard action
             * @param event unused
             * @return <code>true</code> if done key was pressed
             *         <code>false</code> otherwise
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mPortEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPortEditText.getWindowToken(), 0);
                }
                return false;
            }
        });


        mIdEditText = (EditText) findViewById(R.id.id_editText);
        mIdEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            /**
             * Clear cursor and hide keyboard when input is terminated.
             *
             * @param v unused
             * @param actionId identifier of the keyboard action
             * @param event unused
             * @return <code>true</code> if done key was pressed
             *         <code>false</code> otherwise
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mIdEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mIdEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        checkHistory();

        Button mCreateButton = (Button) findViewById(R.id.create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isBlank(mHostEditText) && !isBlank(mPortEditText)) {
                    String host = mHostEditText.getText().toString();
                    int port = Integer.parseInt(mPortEditText.getText().toString());
                    connection = new Connection(host, port, MainActivity.this);

                    try {
                        connection.execute();
                    } catch (Exception e) {
                        toast(R.string.err_concurrent);
                    }

                } else {
                    toast(R.string.err_empty_fields);
                }
            }
        });
    }
}
