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
    private static final int BUF_SIZE = 1000;

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

        char[] chars = new char[BUF_SIZE];
        int size = br.read(chars);

        String response = "";
        if (size > 0) {
            response = new String(chars).substring(0, size);
        }
        System.out.println(response);
        return response;
    }
}
