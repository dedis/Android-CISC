package com.epfl.dedis.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP {

    private static final int TIMEOUT = 1000;

    public static String open(String host, String port, String type, String message) throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/" + type);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(TIMEOUT);
        http.setRequestMethod("POST");
        http.connect();
        
        OutputStream out = http.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(message);
        writer.flush();
        writer.close();

        InputStreamReader in = new InputStreamReader(http.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String response = br.readLine();
        br.close();
        System.out.println(response);
        return response;
    }
}
