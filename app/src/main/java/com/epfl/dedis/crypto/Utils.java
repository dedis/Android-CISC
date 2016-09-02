package com.epfl.dedis.crypto;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


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

    public static Bitmap encodeQR(String message) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(message, BarcodeFormat.QR_CODE, 550, 550);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(550, 550, Bitmap.Config.RGB_565);
        for (int x = 0; x < 550; x++){
            for (int y = 0; y < 550; y++){
                bitmap.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}