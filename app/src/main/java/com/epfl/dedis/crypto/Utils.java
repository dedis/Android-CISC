package com.epfl.dedis.crypto;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.ShortBuffer;
import java.util.UUID;

public class Utils {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();
    
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

    public static Bitmap encodeQR(String message, int px) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, px, px);

        Bitmap bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.RGB_565);
        short[] array = new short[px * px];
        for (int x = 0; x < px; x++) {
            for (int y = 0; y < px; y++) {
                array[x * px + y] = matrix.get(x, y) ? (short)Color.BLACK : (short)Color.WHITE;
            }
        }
        bitmap.copyPixelsFromBuffer(ShortBuffer.wrap(array));
        return bitmap;
    }

    public static float dpToPixel(int dp, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}