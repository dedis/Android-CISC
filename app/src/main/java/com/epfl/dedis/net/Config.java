package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;
import com.google.gson.annotations.SerializedName;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Config {

    @SerializedName("Threshold")
    private int threshold;

    @SerializedName("Device")
    private Map<String, String> device;

    @SerializedName("Data")
    private Map<String, String> data;

    public Config(int threshold, String name, PublicKey pub){
        this.threshold = threshold;

        String pubStr = Ed25519.PubString(pub);
        this.device = new HashMap<>();
        this.device.put(name, pubStr);
        this.data = new HashMap<>();
    }

    public Config(int threshold, Map<String, String> device, Map<String, String> data) {
        this.threshold = threshold;
        this.device = new HashMap<>(device);
        this.data = new HashMap<>(data);
    }

    public Config(Config config) {
        this(config.getThreshold(), config.getDeviceB64(), config.getData());
    }

    public Map<String, PublicKey> getDevice() {
        Map<String, PublicKey>devices = new HashMap<>();
        for (Map.Entry<String, String> entry : device.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            devices.put(key, Ed25519.StringToPub(value));
        }
        return devices;
    }

    public Map<String, String> getDeviceB64() {
        return device;
    }

    public Map<String, String> getData() {
        return data;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public String toString() {
        return "Threshold: " + threshold + "\n" +
                "Device: " + Arrays.toString(device.entrySet().toArray()) + "\n" +
                "Data: " + Arrays.toString(data.entrySet().toArray());
    }
}
