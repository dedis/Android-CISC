package com.epfl.dedis.net;

import java.util.Map;

public class Config {
    private int threshold;
    private Map<String, byte[]> devices;
    private Map<String, String> data;

    public Config(int threshold, Map<String, byte[]> devices, Map<String, String> data) {
        this.threshold = threshold;
        this.devices = devices;
        this.data = data;
    }
}
