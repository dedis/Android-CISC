package com.epfl.dedis.net;

import java.util.Map;

public class Config {
    private int Threshold;
    private Map<String, byte[]> Device;
    private Map<String, String> Data;

    public Config(int Threshold, Map<String, byte[]> Device, Map<String, String> Data) {
        this.Threshold = Threshold;
        this.Device = Device;
        this.Data = Data;
    }
}
