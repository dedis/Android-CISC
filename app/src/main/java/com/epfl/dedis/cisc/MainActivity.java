package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
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

    private EditText mHostTextfield; /* EditText field for hostname */
    private EditText mPortTextfield; /* EditText field for portname */
    private Button mRequestButton;   /* Request connection button */
    private TextView mMessageView;   /* TextView pane for reply */
    private InputMethodManager imm;  /* IMM to handle window peripherals */

    private Connection connection;   /* Connection facility */

    private SharedPreferences preferences;                  /* Key-value storage */
    private static final String HOST_KEY = "hostname";      /* Hostname key */
    private static final String PORT_KEY = "portnumber";    /* Portnumber key */


    /**
     * Check if all fields in the main activity are filled
     * with text.
     *
     * @param hostField string from the hostname field
     * @param portField string from the portname field
     * @return          <code>true</code> if all fields are filled in
     *                  <code>false</code> otherwise
     */
    private boolean isEmptyFields(String hostField, String portField) {
        if (hostField.isEmpty() || portField.isEmpty()) {
            toast(R.string.empty_fields);
            return false;
        }
        return true;
    }

    /**
     * Fetch text from text fields and create a new connection
     * to the Cothority server.
     * <p>
     * Note: Connection management runs concurrently with the
     * main thread. By calling get on the execution the threads
     * are joined.
     *
     * @return <code>true</code> if connection was established successfully
     *         <code>false</code> otherwise
     */
    private void establishConnection() {
        String hostField = mHostTextfield.getText().toString();
        String portField = mPortTextfield.getText().toString();
        if (isEmptyFields(hostField, portField)) {
            connection = new Connection(hostField, Integer.parseInt(portField), this);
            try {
                connection.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
        mHostTextfield.setText(preferences.getString(HOST_KEY, ""));
        mPortTextfield.setText(preferences.getString(PORT_KEY, ""));
    }

    /**
     * Whenever a connection is requested, the entered information
     * in the fields is stored in the internal key-value storage.
     */
    public void writeHistory() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HOST_KEY, mHostTextfield.getText().toString());
        editor.putString(PORT_KEY, mPortTextfield.getText().toString());
        editor.commit();
    }

    /**
     * Place a string into the message textfield in the main activity.
     *
     * @param message string to be displayed
     */
    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHostTextfield = (EditText) findViewById(R.id.host_textfield);
        mHostTextfield.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    mHostTextfield.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mHostTextfield.getWindowToken(), 0);
                }
                return false;
            }
        });
        mPortTextfield = (EditText) findViewById(R.id.port_textfield);
        mPortTextfield.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    mPortTextfield.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPortTextfield.getWindowToken(), 0);
                }
                return false;
            }
        });

        checkHistory();
        mMessageView = (TextView) findViewById(R.id.message_view);

        mRequestButton = (Button) findViewById(R.id.request_button);
        mRequestButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The request button triggers the connection
             * establishment and the log activity.
             *
             * @param v Window view object
             */
            @Override
            public void onClick(View v) {
                establishConnection();
            }
        });
    }
}
