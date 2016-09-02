package com.epfl.dedis.crypto;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.UUID;

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

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static <T> String toJson(T object) {
        return GSON.toJson(object);
    }

    public static String encodeBase64(byte[] array){
        return BaseEncoding.base64().encode(array);
    }

    public static byte[] decodeBase64(String string){
        return BaseEncoding.base64().decode(string);
    }
}