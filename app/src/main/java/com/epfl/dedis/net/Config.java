package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private int threshold;
    private Map<String, String> device;
    private Map<String, String> data;

    public Config(int threshold, String name, PublicKey pub){
        this.threshold = threshold;

        String pubStr = Ed25519.PubString(pub);
        device = new HashMap<>();
        device.put(name, pubStr);
    }

    public Config(int threshold, Map<String, String> device, Map<String, String> Data) {
        this.threshold = threshold;
        this.device = device;
        this.data = data;
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
}
