package ch.epfl.dedis.net;

import android.util.Log;

import ch.epfl.dedis.cisc.ConfigActivity;
import ch.epfl.dedis.crypto.Ed25519;
import com.google.common.collect.Iterables;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * The Identity bundles the all the information about a Skipchain
 * and the device that is currently connected to it. This class
 * serves as the principal data structure from which all request
 * are made.
 */
public class Identity {

    private static final String TAG = "net.Identity";

    private String mName;
    private byte[] mId;
    private byte[] mSeed;
    private byte[] mRSASecret;

    private Cothority mCothority;
    private Config mConfig;
    private Config mProposed;
    private ConfigActivity.ConfigState mConfigState;

    public Identity(String name, Cothority cothority) {
        mName = name;
        mCothority = cothority;

        KeyPair keyPair = Ed25519.newKeyPair();
        mSeed = ((EdDSAPrivateKey)keyPair.getPrivate()).getSeed();
        mConfig = new Config(3, name, keyPair.getPublic());
    }

    // Debugging constructor
    public Identity(Cothority cothority, byte[] id, ConfigActivity.ConfigState configState) {
        mCothority = cothority;
        mId = id;
        mConfigState = configState;
    }

    /**
     * In case the Identity doesn't have a owner yet a new
     * one is created by setting the configuration to proposed.
     *
     * @param name Device owner's identification
     */
    public void newDevice(String name) {
        Log.d(TAG, "Add device " + name);
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
        Log.d(TAG, "Update data " + data);
        mProposed = new Config(mConfig);
        mProposed.getData().put(mName, data);
    }

    public PublicKey getPublic() {
        EdDSAPublicKeySpec epks = new EdDSAPublicKeySpec(getPrivate().getA(), Ed25519.getCurveSpec());
        return new EdDSAPublicKey(epks);
    }

    public EdDSAPrivateKey getPrivate() {
        EdDSAPrivateKeySpec epks = new EdDSAPrivateKeySpec(mSeed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(epks);
    }

    public String getName() {
        return mName;
    }

    public byte[] getId() {
        return mId;
    }

    public Cothority getCothority() {
        return mCothority;
    }

    public Config getConfig() {
        return mConfig;
    }

    public Config getProposed() {
        return mProposed;
    }

    public void setId(byte[] id) {
        mId = id;
    }

    public void setRSASecret(byte[] rsaSecret) {
        mRSASecret = rsaSecret;
    }

    public void setConfig(Config config) {
        mConfig = config;
    }

    public void setProposed(Config proposed) {
        mProposed = proposed;
    }

    public ConfigActivity.ConfigState getConfigState() {
        return mConfigState;
    }

    public void setConfigState(ConfigActivity.ConfigState configState) {
        mConfigState = configState;
    }

    /**
     * Create pretty string of the proposed configuration.
     *
     * @return String of proposed feature
     */
    public String getProposalString() {
        if (mProposed != null) {
            Map<String, String> configDevice = new HashMap<>(mConfig.getDevice());
            Map<String, String> configData = new HashMap<>(mConfig.getData());
            Map<String, String> proposedDevice = new HashMap<>(mProposed.getDevice());
            Map<String, String> proposedData = new HashMap<>(mProposed.getData());

            proposedDevice.entrySet().removeAll(configDevice.entrySet());
            proposedData.entrySet().removeAll(configData.entrySet());

            Map.Entry<String, String> device;
            Map.Entry<String, String> data;
            if (!proposedDevice.entrySet().isEmpty()) {
                device = Iterables.getOnlyElement(proposedDevice.entrySet());
                return "New proposal:\n\n" + "Name: " + device.getKey().substring(0, 15) +
                        "\nPubKey: " + device.getValue().substring(0, 15);
            } else if (!proposedData.entrySet().isEmpty()) {
                data = Iterables.getOnlyElement(proposedData.entrySet());
                return "New proposal:\n\n" + "Name: " + data.getKey().substring(0, 15) +
                        "\nSSH: " + data.getValue().substring(0, 15);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
 }