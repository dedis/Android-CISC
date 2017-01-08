package ch.epfl.dedis.crypto;

import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.google.common.io.BaseEncoding;

/**
 * Ed25519 key pair factory.
 *
 * @author Andrea Caforio
 */
public class Ed25519 {

    private final PublicKey pub;
    private final PrivateKey priv;

    public Ed25519() {
        KeyPair keyPair = new KeyPairGenerator().generateKeyPair();
        this.pub = keyPair.getPublic();
        this.priv = keyPair.getPrivate();
    }

    public PrivateKey getPrivate() {
        return priv;
    }

    public PublicKey getPublic(){
        return pub;
    }

    /**
     * Static factory method.
     *
     * @return KeyPair object
     */
    public static KeyPair newKeyPair() {
        return new KeyPairGenerator().generateKeyPair();
    }

    public static EdDSANamedCurveSpec getCurveSpec(){
        return EdDSANamedCurveTable.getByName("ed25519-sha-512");
    }

    public static String PubString(PublicKey pub){
        return BaseEncoding.base64().encode(Ed25519.PubBytes(pub));
    }

    public static PublicKey StringToPub(String pub){
        return Ed25519.BytesToPub(BaseEncoding.base64().decode(pub));
    }

    public static byte[] PubBytes(PublicKey pub){
        return ((EdDSAPublicKey)pub).getAbyte();
    }

    public static PublicKey BytesToPub(byte[] pub){
        EdDSAPublicKeySpec pubSpec = new EdDSAPublicKeySpec(pub, Ed25519.getCurveSpec());
        return new EdDSAPublicKey(pubSpec);
    }
}
