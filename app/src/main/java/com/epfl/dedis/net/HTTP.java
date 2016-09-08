package com.epfl.dedis.net;

import android.os.AsyncTask;

import com.epfl.dedis.api.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The HTTP class is the network socket of this project. It handles all
 * communication with the Cothority service through the standard HTTP-
 * protocol. The socket is fully concurrent to avoid any blocking of the
 * main thread.
 */
public class HTTP extends AsyncTask<Void, Void, String> {

    private static final int TIMEOUT = 2000;
    private static final int BUF_SIZE = 10000;

    private static final String ERR_COTHORITY = "1";
    private static final String ERR_NETWORK = "2";

    private Message _message;
    private Cothority _cothority;
    private String _path;
    private String _json;

    private int responseCode;

    public HTTP(Message message, Cothority cothority, String path, String json) {
        _message = message;
        _cothority = cothority;
        _path = path;
        _json = json;
    }

    /**
     * Spawns a new thread to deal with the network traffic. All acknowledgements
     * from the Cothority are parsed to Strings.
     *
     * @param params Unused in this context.
     * @return Acknowledgement message from the Cothority.
     */
    @Override
    public String doInBackground(Void... params) {
        try {
            System.out.println("OUT: " + _json);
            URL url = new URL("http://" + _cothority.getHost() + ":" + _cothority.getPort() + "/" + _path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(TIMEOUT);
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            OutputStream out = http.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(_json);
            writer.flush();
            writer.close();

            responseCode = http.getResponseCode();
            if (responseCode != 200) {
                http.disconnect();
                return "";
            }

            InputStreamReader in = new InputStreamReader(http.getInputStream());
            BufferedReader br = new BufferedReader(in);

            char[] chars = new char[BUF_SIZE];
            int size = br.read(chars);

            String response = new String(chars).substring(0, size);
            http.disconnect();
            System.out.println("IN: " + response);
            return response;
        } catch (IOException e) {
            responseCode = 400;
            return "";
        }
    }

    /**
     * Joins the thread spawned by doInBackground and passes the
     * result to the calling activity.
     *
     * @param result Acknowledgement from doInBackground.
     */
    @Override
    public void onPostExecute(String result) {
        if (result.isEmpty()) {
            _message.callbackError(responseCode);
        } else {
            _message.callback(result);
        }
    }
}
