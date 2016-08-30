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

    private String _name;
    private byte[] _id;
    private byte[] _seed;

    private Cothority _cothority;
    private Config _config;
    private Config _proposed;

    public Identity(String name, Cothority cothority) {
        _name = name;
        _cothority = cothority;

        KeyPair keyPair = Ed25519.newKeyPair();
        _seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        _config = new Config(3, name, keyPair.getPublic());
    }

    // Debugging constructor
    public Identity(Cothority cothority, byte[] id) {
        _cothority = cothority;
        _id = id;
    }

    /**
     * In case the Identity doesn't have a owner yet a new
     * one is created by setting the configuration to proposed.
     *
     * @param name Device owner's identification
     */
    public void newDevice(String name) {
        _name = name;
        _proposed = new Config(_config);

        KeyPair keyPair = Ed25519.newKeyPair();
        _seed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        _proposed.getDevice().put(name, Ed25519.PubString(keyPair.getPublic()));
    }

    /**
     * Modify or add data for the device owner then set configuration
     * as proposed.
     *
     * @param data Associated to the device owner
     */
    public void updateData(String data) {
        _proposed = new Config(_config);
        _proposed.getData().put(_name, data);
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
        EdDSAPrivateKeySpec epks = new EdDSAPrivateKeySpec(_seed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(epks);
    }

    /**
     * @return Device owner's name
     */
    public String getName() {
        return _name;
    }

    /**
     * @return Skipchain ID
     */
    public byte[] getId() {
        return _id;
    }

    /**
     * @return Cothority (Network information)
     */
    public Cothority getCothority() {
        return _cothority;
    }

    /**
     * @return Current Configuration
     */
    public Config getConfig() {
        return _config;
    }

    /**
     * @return Proposed Configuration
     */
    public Config getProposed() {
        return _proposed;
    }

    /**
     * @param id Skipchain identity
     */
    public void setId(byte[] id) {
        _id = id;
    }

    /**
     * @param config Configuration
     */
    public void setConfig(Config config) {
        _config = config;
    }

    /**
     * @param proposed Configuration
     */
    public void setProposed(Config proposed) {
        _proposed = proposed;
    }
}