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

public class HTTP extends AsyncTask<String, Void, String> {

    private static final int TIMEOUT = 1000;
    private static final int BUF_SIZE = 1000;

    private static final String ERR_COTHORITY = "1";
    private static final String ERR_NETWORK = "2";

    private Message message;
    private Cothority cothority;
    private String path;
    private String json;

    public HTTP(Message message, Cothority cothority, String path, String json) {
        this.message = message;
        this.cothority = cothority;
        this.path = path;
        this.json = json;
    }

    @Override
    public String doInBackground(String... params) {
        try {
            URL url = new URL("http://" + cothority.getHost() + ":" + cothority.getPort() + "/" + path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(TIMEOUT);
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            OutputStream out = http.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(json);
            writer.flush();
            writer.close();

            InputStreamReader in = new InputStreamReader(http.getInputStream());
            BufferedReader br = new BufferedReader(in);

            char[] chars = new char[BUF_SIZE];
            int size = br.read(chars);

            if (chars[0] == '0') {
                http.disconnect();
                return ERR_COTHORITY;
            }

            String response = new String(chars).substring(0, size);
            //System.out.println(path + " " + response + "\n");
            http.disconnect();
            return response;
        } catch (IOException e) {
            return ERR_NETWORK;
        }
    }

    @Override
    public void onPostExecute(String result) {
        message.callback(result);
    }
}
