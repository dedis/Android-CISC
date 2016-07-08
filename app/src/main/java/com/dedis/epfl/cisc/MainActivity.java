package com.dedis.epfl.cisc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
    private File history;            /* Logfile for connection history */

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
     * history file in the application's internal storage.
     * If said file can be successfully located and opened
     * the first line is split and entered into the text
     * fields.
     */
    private void checkHistory() {
        history = new File(getApplicationContext().getFilesDir(), "history.log");
        if (history.isFile()) {
            try {
                FileInputStream input = new FileInputStream(history);
                Scanner scanner = new Scanner(input);
                String[] split = scanner.next().split(";");
                mHostTextfield.setText(split[0]);
                mPortTextfield.setText(split[1]);
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Whenever a connection is requested a new history logfile is
     * created in the internal storage and the inputs in the text
     * fields is written to the first line.
     * <p>
     * File format: hostname and port number are seperated by
     * a semicolon.
     */
    private void writeHistory() {
        try {
            history.createNewFile();
            FileWriter writer = new FileWriter(history);
            writer.write(mHostTextfield.getText().toString() + ";" + mPortTextfield.getText().toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
             * @param v
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
