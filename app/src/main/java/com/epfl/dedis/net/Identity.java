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
    private String name;
    private Cothority cothority;

    private Config config;
    private Config proposed;

    public Identity(String name) {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        this.seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        this.name = name;
        this.config = new Config(3, name, keyPair.getPublic());
    }

    public Identity(String name, Cothority cothority) {
        this(name);
        this.cothority = cothority;
    }

    public Identity(Cothority cothority, byte[] skipchainId) {
        this.cothority = cothority;
        this.skipchainId = skipchainId;
    }

    public void newDevice(String name) {
        this.name = name;
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        proposed = new Config(config);
        proposed.getDeviceB64().put(this.name, Ed25519.PubString(keyPair.getPublic()));
    }

    public static Identity load(String str){
        return Utils.fromJson(str, Identity.class);
    }

    public String save(){
        return Utils.toJson(this);
    }

    public PublicKey getPub() {
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(getEdDSAPrivate().getA(), Ed25519.getCurveSpec());
        return new EdDSAPublicKey(pubKey);
    }

    public PrivateKey getPrivate() {
        return getEdDSAPrivate();
    }

    public EdDSAPrivateKey getEdDSAPrivate() {
        EdDSAPrivateKeySpec key = new EdDSAPrivateKeySpec(seed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(key);
    }

    public byte[] getSkipchainId() {
        return skipchainId;
    }

    public String getName() {
        return name;
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

    public void setProposed(Config proposed) {
        this.proposed = proposed;
    }

    //    public int[] getPubEncoded() {
//        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(getEdDSAPrivate().getA(), Ed25519.getCurveSpec());
//        return Utils.byteArrayToIntArray(new EdDSAPublicKey(pubKey).getEncoded());
//    }
//
//    public int[] getPrivateEncoded() {
//        return Utils.byteArrayToIntArray(getEdDSAPrivate().getEncoded());
//    }
}
