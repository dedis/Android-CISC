package com.epfl.dedis.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPSocket {

    private static final int TIMEOUT = 1000;
    private static final int SIZE = 1024;

    public static String open(String host, int port, String message) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT);

        OutputStream outFromServer = socket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outFromServer);
        out.writeBytes(message);

        InputStream inFromServer = socket.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);

        byte[] buffer = new byte[SIZE];
        int size = in.read(buffer);
        socket.close();

        return String.valueOf(size);
    }
}
