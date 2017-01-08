package ch.epfl.dedis.net;

import android.util.Log;

import ch.epfl.dedis.crypto.Ed25519;
import ch.epfl.dedis.crypto.Utils;
import com.google.gson.annotations.SerializedName;

import net.i2p.crypto.eddsa.EdDSAEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * The Skipblock class bundles the top level fields of a block
 * in the Skipchain. It is further used to verify the hash and
 * the signature.
 *
 * @author Andrea Caforio
 */
public class SkipBlock {

    private static final String TAG = "net.SkipBlock";

    @SerializedName("SkipBlockFix")
    SkipBlockFix mFix;

    @SerializedName("Hash")
    String mId;

    @SerializedName("Sig")
    String mSig;

    @SerializedName("Msg")
    String mMsg;

    public SkipBlockFix getFix() {
        return mFix;
    }

    public String getId() {
        return mId;
    }

    public boolean verifyBlock() {
        return verifyHash() && verifySignature();
    }

    /**
     * The SHA256 hash is created by adding the field of Skipblock and
     * SkipblockFix in the correct order and format. In the end it must
     * be equal to the identifier of the block.
     *
     * @return true if constructed hash equals the identifier
     */
    public boolean verifyHash() {
        Log.d(TAG, "Verify hash.");
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

            // Integers are added in the eight byte little endian format.
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(mFix.mIndex);
            sha256.update(buffer.array());

            buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(mFix.mHeight);
            sha256.update(buffer.array());

            buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(mFix.mMaximumHeight);
            sha256.update(buffer.array());

            buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(mFix.mBaseHeight);
            sha256.update(buffer.array());

            // Strings are added in base64.
            for (String s : mFix.mBackLinkIds) {
                sha256.update(Utils.decodeBase64(s));
            }

            sha256.update(Utils.decodeBase64(mFix.mVerifierId));
            sha256.update(Utils.decodeBase64(mFix.mParentBlockId));
            sha256.update(Utils.decodeBase64(mFix.mAggregate));
            sha256.update(Utils.decodeBase64(mFix.mAggregateResp));
            sha256.update(Utils.decodeBase64(mFix.mData));

            byte[] hash = sha256.digest();
            return Arrays.equals(hash, Utils.decodeBase64(mId));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * To verify the block signature, the aggregate block signature needs
     * to equal the signature field.
     *
     * @return true if aggregate signature corresponds to signature field
     */
    public boolean verifySignature() {
        Log.d(TAG, "Verify signature.");
        byte[] aggregate = Utils.decodeBase64(mFix.mAggregate);
        PublicKey pb = Ed25519.BytesToPub(aggregate);

        EdDSAEngine engine = new EdDSAEngine();

        try {
            byte[] signature = Utils.decodeBase64(mSig);
            byte[] message = Utils.decodeBase64(mMsg);

            engine.initVerify(pb);

            // Only use the first 64 bytes of the signature.
            byte[] byteSig = Arrays.copyOfRange(signature, 0, 64);
            return engine.verifyOneShot(message, byteSig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
