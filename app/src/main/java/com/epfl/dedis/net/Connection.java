package com.epfl.dedis.net;

import android.os.AsyncTask;
import java.net.*;
import java.io.*;

import com.epfl.dedis.cisc.MainActivity;
import com.epfl.dedis.cisc.R;

/**
 * Opening a connection to a Cothority server
 * and parsing its reply. The class extends
 * the AsyncTask concurrency model that is required
 * by default to handle network connections.
 *
 * @author Ignacio Aléman
 * @author Andrea Caforio
 * @since 08.07.16
 */
public class Connection extends AsyncTask<Void, Boolean, Boolean> {

    private String hostname;                    /* Cothority address */
    private int port;                           /* Cothority port number */

    private Socket socket;                      /* Connection tunnel */

    private byte[] publicKey;                   /* Array to hold Cothority's public key */
    private static final int KEY_SIZE = 100;    /* Presumbtive? public key size */

    private MainActivity mainActivity;          /* Reference to the main activity */

    public Connection(String hostname, int port, MainActivity mainActivity) {
        this.hostname = hostname;
        this.port = port;

        this.publicKey = new byte[KEY_SIZE];

        this.mainActivity = mainActivity;
    }

    /**
     * Turn the byte array from the server acknowledgement
     * into a single string.
     *
     * @return server acknowledgement string
     */
    private String publicKeyToString() {
        StringBuffer buffer = new StringBuffer();
        for (byte b : publicKey) {
            buffer.append(b);
        }
        return buffer.toString();
    }

    /**
     * Actual background task that handle the connection
     * establishment. Meaning that a socket is opened to the
     * Cothority server and its acknowledgement message is
     * stored.
     *
     * @param params Unused argument ellipse
     * @return       <code>true</code> if connection was successful
     *               <code>false</code> otherwise
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 1000);
            if (socket.isConnected()) {
                InputStream inFromServer = socket.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                in.read(publicKey);
                socket.close();
                return true;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Called after the asynchronous task is terminated. The argument
     * indicates if the handshake with the Cothority server was
     * successful and a corresponding toast is then displayed.
     *
     * @param result bool indicator for asynchronous thread
     */
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mainActivity.setMessage(publicKeyToString());
            mainActivity.writeHistory();
            mainActivity.toast(R.string.successful_connection);
        } else {
            mainActivity.toast(R.string.failed_connection);
        }
    }
}
