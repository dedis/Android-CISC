package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Identity {
    private byte[] seed;
    private byte[] skipchainId;
    private String deviceName;
    private Cothority cothority;

    private Config config;
    private Config proposed;

    private Gson gson;

    public Identity(String name){
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        deviceName = name;
        config = new Config(3, name, keyPair.getPublic());
    }

    public Identity(String name, Cothority cot){
        this(name);
        cothority = cot;
    }

    public static Identity load(String str){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(str, Identity.class);
    }

    public String save(){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }


    private int[] byteToIntArray(byte[] array) {
        int[] conv = new int[array.length];
        for (int i = 0; i < conv.length; i++) {
            if (array[i] < 0) {
                conv[i] = array[i] + 256;
            } else {
                conv[i] = array[i];
            }
        }
        return conv;
    }

    public PublicKey getPub() {
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(getEdDSAPrivate().getA(), Ed25519.getCurveSpec());
        return new EdDSAPublicKey(pubKey);
    }

    public PrivateKey getPrivate() {
        return getEdDSAPrivate();
    }

    public int[] getPubEncoded() {
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(getEdDSAPrivate().getA(), Ed25519.getCurveSpec());
        return byteToIntArray(new EdDSAPublicKey(pubKey).getEncoded());
    }

    public int[] getPrivateEncoded() {
        return byteToIntArray(getEdDSAPrivate().getEncoded());
    }

    public EdDSAPrivateKey getEdDSAPrivate() {
        EdDSAPrivateKeySpec key = new EdDSAPrivateKeySpec(seed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(key);
    }

    public byte[] getSkipchainId() {
        return skipchainId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Cothority getCothority() {
        return cothority;
    }

    public Config getConfig() {
        return config;
    }

    public Config getProposed() {
        return proposed;
    }

    public void setSkipchainId(byte[] skipchainId) {
        this.skipchainId = skipchainId;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
