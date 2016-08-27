package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;
import com.epfl.dedis.crypto.Utils;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
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

    //private transient KeyPair keyPair;

    public Identity(String name) {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        this.seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        this.deviceName = name;
        this.config = new Config(3, name, keyPair.getPublic());
    }

    public Identity(String name, Cothority cothority) {
        this(name);
        this.cothority = cothority;
    }

    public Identity(String name, Cothority cothority, byte[] skipchainId) {
        this.deviceName = name;
        this.cothority = cothority;
        this.skipchainId = skipchainId;
    }

    public void newDevice() {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        proposed = new Config(config);
        proposed.getDeviceB64().put(deviceName + "1", Ed25519.PubString(keyPair.getPublic()));
    }

    public static Identity load(String str){
        return Utils.GSON.fromJson(str, Identity.class);
    }

    public String save(){
        return Utils.GSON.toJson(this);
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
        return Utils.byteArrayToIntArray(new EdDSAPublicKey(pubKey).getEncoded());
    }

    public int[] getPrivateEncoded() {
        return Utils.byteArrayToIntArray(getEdDSAPrivate().getEncoded());
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
