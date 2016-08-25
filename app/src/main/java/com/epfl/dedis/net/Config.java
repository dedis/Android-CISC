package com.epfl.dedis.net;

import org.apache.commons.codec.binary.Base64;

import com.epfl.dedis.crypto.Ed25519;

import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private int Threshold;
    private Map<String, String> Devices;
    private Map<String, String> Data;

    public Config(int threshold, String name, PublicKey pub){
        Threshold = threshold;
        String pubStr = Ed25519.PubString(pub);
        Devices = new HashMap<>();
        Devices.put(name, pubStr);
    }

    public Config(int Threshold, Map<String, String> Device, Map<String, String> Data) {
        Threshold = Threshold;
        Devices = Device;
        Data = Data;
    }

    public Map<String, PublicKey> getDevices() {
        Map<String, PublicKey>devices = new HashMap<>();
        for (Map.Entry<String, String> entry : Devices.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            devices.put(key, Ed25519.StringToPub(value));
        }
        return devices;
    }

    public Map<String, String> getData() {
        return Data;
    }
}
