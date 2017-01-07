package ch.epfl.dedis.crypto;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class Utils {

    private static final String TAG = "crypto.Utils";

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(QRStamp.class, new QRStampSerializer())
            .serializeNulls()
            .disableHtmlEscaping()
            .create();
    
    public static String uuid() {
        Log.d(TAG, "Generate UUID.");
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
        Log.d(TAG, "Encode: " + message);
        MultiFormatWriter writer = new MultiFormatWriter();
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, px, px, hintMap);

        int width = matrix.getWidth();
        int height = matrix.getHeight();

        int[] array = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                array[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(array, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static float dpToPixel(int dp, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}