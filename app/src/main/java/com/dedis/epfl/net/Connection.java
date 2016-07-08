package com.dedis.epfl.net;

import android.os.AsyncTask;
import java.net.*;
import java.io.*;

//TODO unit tests
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
public class Connection extends AsyncTask<Void, Void, Boolean> {

    private String hostname; /* Cothority address */
    private int port;        /* Cothority port number */

    private Socket socket;   /* Connection tunnel */

    private byte[] reply;    /* Array to hold acknowledgement */
    // TODO how to know message size?
    private static final int REPLY_SIZE = 100;

    public Connection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        this.reply = new byte[REPLY_SIZE];
    }

    /**
     * Turn the byte array from the server acknowledgement
     * into a single string.
     *
     * @return server acknowledgement string
     */
    public String getReply() {
        StringBuffer buffer = new StringBuffer();
        for (byte b : reply) {
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
            socket = new Socket(hostname, port);

            //OutputStream outToServer = server.getOutputStream();
            //DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = socket.getInputStream();

            DataInputStream in = new DataInputStream(inFromServer);
            in.read(reply); //Read ack. into array

            //TODO How to correctly end a connection?
            //server.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
