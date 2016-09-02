package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;
import com.google.gson.annotations.SerializedName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
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
    private int _threshold;

    @SerializedName("Device")
    private Map<String, String> _device;

    @SerializedName("Data")
    private Map<String, String> _data;

    public Config(int threshold, String name, PublicKey pub){
        _threshold = threshold;
        _device = new TreeMap<>();
        _device.put(name, Ed25519.PubString(pub));
        _data = new HashMap<>();
        _data.put(name, null);
    }

    // Copy constructor
    public Config(int threshold, Map<String, String> device, Map<String, String> data) {
        _threshold = threshold;
        _device = new HashMap<>(device);
        _data = new HashMap<>(data);
    }

    public Config(Config that) {
        this(that.getThreshold(), that.getDevice(), that.getData());
    }

    public void addData(String owner, String data) {
        _data.put(owner, data);
    }

    public byte[] hash() throws Exception {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-256");

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(_threshold);

        sha512.update(buffer.array());

        for (Map.Entry<String, String> entry : _device.entrySet()) {
            sha512.update(entry.getKey().getBytes());

            String value = _data.get(entry.getKey());
            if (value != null) {
                sha512.update(value.getBytes());
            }
            PublicKey pub = Ed25519.StringToPub(entry.getValue());
            sha512.update(Ed25519.PubBytes(pub));
        }

        return sha512.digest();
    }

    /**
     * @return Skipchain voting threshold
     */
    public int getThreshold() {
        return _threshold;
    }

    /**
     * @return Devices with their corresponding public keys
     */
    public Map<String, String> getDevice() {
        return _device;
    }

    /**
     * @return Devices with their corresponding data
     */
    public Map<String, String> getData() {
        return _data;
    }
}
