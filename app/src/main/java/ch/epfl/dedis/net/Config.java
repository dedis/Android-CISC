package ch.epfl.dedis.net;

import android.util.Log;

import ch.epfl.dedis.crypto.Ed25519;
import ch.epfl.dedis.crypto.Utils;
import com.google.gson.annotations.SerializedName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The config class holds the information about all devices
 * currently stored in a Skipchain. This structure is maintained
 * locally on the device and its fields are serialized when
 * communication with a Cothority is needed.
 *
 * @author Andrea Caforio
 */
public class Config {

    private static final String TAG = "net.Config";

    @SerializedName("Threshold")
    private final int mThreshold;

    @SerializedName("Device")
    private Map<String, String> mDevice;

    @SerializedName("Data")
    private Map<String, String> mData;

    public Config(int threshold, String name, PublicKey pub){
        mThreshold = threshold;
        mDevice = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        mDevice.put(name, Ed25519.PubString(pub));
        mData = new HashMap<>();
        mData.put(name, null);
    }

    // Copy constructor
    public Config(int threshold, Map<String, String> device, Map<String, String> data) {
        mThreshold = threshold;
        mDevice = new HashMap<>(device);
        mData = new HashMap<>(data);
    }

    public Config(Config that) {
        this(that.getThreshold(), that.getDevice(), that.getData());
    }

    /**
     * Calculate the SHA256 hash of the configuration. Here the order in which
     * the fields are added is crucial.
     *
     * @return byte array hash
     * @throws NoSuchAlgorithmException
     */
    public byte[] hash() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        // Threshold added as little endian 4 byte integer.
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(mThreshold);
        sha256.update(buffer.array());

        // Sort device names lexicographically and add their byte representation.
        List<String> sorted = new ArrayList<>(mDevice.keySet());
        Collections.sort(sorted);
        for (String key : sorted) {
            sha256.update(key.getBytes());

            String value = mData.get(key);
            if (value != null) {
                sha256.update(value.getBytes());
            }
            // Public key added in byte representation.
            PublicKey pub = Ed25519.StringToPub(mDevice.get(key));
            sha256.update(Ed25519.PubBytes(pub));
        }
        Log.d(TAG, "Calculated hash.");
        return sha256.digest();
    }

    public int getThreshold() {
        return mThreshold;
    }

    public Map<String, String> getDevice() {
        return mDevice;
    }

    public Map<String, String> getData() {
        return mData;
    }

    public void setData(Map<String, String> data) {
        mData = data;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
