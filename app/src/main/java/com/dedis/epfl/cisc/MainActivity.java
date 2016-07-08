package com.dedis.epfl.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dedis.epfl.net.Connection;

//TODO unit tests
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
    private boolean isEmpty(String hostField, String portField) {
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
    private boolean establishConnection() {
        String hostField = mHostTextfield.getText().toString();
        String portField = mPortTextfield.getText().toString();
        if (isEmpty(hostField, portField)) {
            connection = new Connection(hostField, Integer.parseInt(portField));
            try {
                return connection.execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Display common toast marker on the screen.
     *
     * @param text resource id of the string to be displayed
     */
    private void toast(int text) {
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
    private void writeHistory() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HOST_KEY, mHostTextfield.getText().toString());
        editor.putString(PORT_KEY, mPortTextfield.getText().toString());
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHostTextfield = (EditText) findViewById(R.id.host_textfield);
        mPortTextfield = (EditText) findViewById(R.id.port_textfield);
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
                if (establishConnection()) {
                    toast(R.string.successful_connection);
                    mMessageView.setText(connection.getReply());
                    writeHistory();
                } else {
                    toast(R.string.failed_connection);
                }
            }
        });
    }
}
