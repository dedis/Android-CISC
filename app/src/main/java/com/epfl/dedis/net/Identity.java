package com.epfl.dedis.net;

import com.epfl.dedis.crypto.Ed25519;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * The Identity bundles the all the information about a Skipchain
 * and the device that is currently connected to it. This class
 * serves as the principal data structure from which all request
 * are made.
 */
public class Identity {

    private String mName;
    private byte[] mId;
    private byte[] mSeed;
    private byte[] mRSASecret;

    private Cothority mCothority;
    private Config mConfig;
    private Config mProposed;

    public Identity(String name, Cothority cothority) {
        mName = name;
        mCothority = cothority;

        KeyPair keyPair = Ed25519.newKeyPair();
        mSeed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        mConfig = new Config(3, name, keyPair.getPublic());
    }

    // Debugging constructor
    public Identity(Cothority cothority, byte[] id) {
        mCothority = cothority;
        mId = id;
    }

    /**
     * In case the Identity doesn't have a owner yet a new
     * one is created by setting the configuration to proposed.
     *
     * @param name Device owner's identification
     */
    public void newDevice(String name) {
        mName = name;
        mProposed = new Config(mConfig);

        KeyPair keyPair = Ed25519.newKeyPair();
        mSeed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        mProposed.getDevice().put(name, Ed25519.PubString(keyPair.getPublic()));
    }

    /**
     * Modify or add data for the device owner then set configuration
     * as proposed.
     *
     * @param data Associated to the device owner
     */
    public void updateData(String data) {
        mProposed = new Config(mConfig);
        mProposed.getData().put(mName, data);
    }

    /**
     * @return Device owner's public key
     */
    public PublicKey getPublic() {
        EdDSAPublicKeySpec epks = new EdDSAPublicKeySpec(getPrivate().getA(), Ed25519.getCurveSpec());
        return new EdDSAPublicKey(epks);
    }

    /**
     * @return Device owner's private key
     */
    public EdDSAPrivateKey getPrivate() {
        EdDSAPrivateKeySpec epks = new EdDSAPrivateKeySpec(mSeed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(epks);
    }

    /**
     * @return Device owner's name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return Device's owners EdDSA seed
     */
    public byte[] getSeed() {
        return mSeed;
    }

    /**
     * @return Skipchain ID
     */
    public byte[] getId() {
        return mId;
    }

    /**
     * @return RSA private key (SSH)
     */
    public byte[] getRSASecret() {
        return mRSASecret;
    }

    /**
     * @return Cothority (Network information)
     */
    public Cothority getCothority() {
        return mCothority;
    }

    /**
     * @return Current Configuration
     */
    public Config getConfig() {
        return mConfig;
    }

    /**
     * @return Proposed Configuration
     */
    public Config getProposed() {
        return mProposed;
    }

    /**
     * @param id Skipchain identity
     */
    public void setId(byte[] id) {
        mId = id;
    }

    /**
     * @param rsaSecret SSH private key
     */
    public void setRSASecret(byte[] rsaSecret) {
        mRSASecret = rsaSecret;
    }

    /**
     * @param config Configuration
     */
    public void setConfig(Config config) {
        mConfig = config;
    }

    /**
     * @param proposed Configuration
     */
    public void setProposed(Config proposed) {
        mProposed = proposed;
    }
}