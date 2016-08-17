package com.epfl.dedis.net;

import android.os.AsyncTask;
import android.util.Log;

import com.epfl.dedis.cisc.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP extends AsyncTask<String, Void, String> {

    private static final int TIMEOUT = 1000;
    private static final int BUF_SIZE = 1000;

    private static final String ERR_COTHORITY = "1";
    private static final String ERR_NETWORK = "2";

    private Activity activity;

    public HTTP(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://" + params[0] + ":" + params[1] + "/" + params[2]);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(TIMEOUT);
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            OutputStream out = http.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(params[3]);
            writer.flush();
            writer.close();

            InputStreamReader in = new InputStreamReader(http.getInputStream());
            BufferedReader br = new BufferedReader(in);

            char[] chars = new char[BUF_SIZE];
            int size = br.read(chars);

            if (chars[0] == '0') {
                return ERR_COTHORITY;
            }

            String response = new String(chars).substring(0, size);
            Log.i(getClass().getName(), response); // TODO integrate proper logging
            return response;
        } catch (IOException e) {
            return ERR_NETWORK;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        activity.callback(result);
    }
}
