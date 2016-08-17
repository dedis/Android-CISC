package com.epfl.dedis.net;

import java.util.Map;

public class Config {
    private int Threshold;
    private Map<String, int[]> Device;
    private Map<String, String> Data;

    public Config(int Threshold, Map<String, int[]> Device, Map<String, String> Data) {
        this.Threshold = Threshold;
        this.Device = Device;
        this.Data = Data;
    }

    public Map<String, int[]> getDevice() {
        return Device;
    }

    public Map<String, String> getData() {
        return Data;
    }
}
