package com.epfl.dedis.crypto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.i2p.crypto.eddsa.KeyPairGenerator;

import java.security.KeyPair;

public class Utils {

    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static int[] byteArrayToIntArray(byte[] array) {
        int[] conv = new int[array.length];
        for (int i = 0; i < array.length; i++){
            conv[i] = array[i] & 0xff;
        }
        return conv;
    }
}