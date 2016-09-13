package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;
import com.google.gson.annotations.SerializedName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The config class holds the information about all devices
 * currently stored in a Skipchain. This structure is maintained
 * locally on the device and its fields are serialized when
 * communication with a Cothority is needed.
 */
public class Config {

    @SerializedName("Threshold")
    private int mThreshold;

    @SerializedName("Device")
    private Map<String, String> mDevice;

    @SerializedName("Data")
    private Map<String, String> mData;

    public Config(int threshold, String name, PublicKey pub){
        mThreshold = threshold;
        mDevice = new TreeMap<>();
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

    public void addData(String owner, String data) {
        mData.put(owner, data);
    }

    // TODO: Check if hash is really correct
    public byte[] hash() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(mThreshold);
        sha256.update(buffer.array());

        for (Map.Entry<String, String> entry : mDevice.entrySet()) {
            sha256.update(entry.getKey().getBytes());

            String value = mData.get(entry.getKey());
            if (value != null) {
                sha256.update(value.getBytes());
            }
            PublicKey pub = Ed25519.StringToPub(entry.getValue());
            sha256.update(Ed25519.PubBytes(pub));
        }

        return sha256.digest();
    }

    /**
     * @return Skipchain voting threshold
     */
    public int getThreshold() {
        return mThreshold;
    }

    /**
     * @return Devices with their corresponding public keys
     */
    public Map<String, String> getDevice() {
        return mDevice;
    }

    /**
     * @return Devices with their corresponding data
     */
    public Map<String, String> getData() {
        return mData;
    }

    /**
     *
     */
    public void setData(Map<String, String> data) {
        mData = data;
    }
}
