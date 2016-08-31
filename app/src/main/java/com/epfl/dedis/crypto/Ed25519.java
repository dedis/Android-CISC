package com.epfl.dedis.crypto;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

//import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

//import android.util.Base64;

import com.google.common.io.BaseEncoding;

public class Ed25519 {

    private PublicKey pub;
    private PrivateKey priv;

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

    public static KeyPair newKeyPair() {
        return new KeyPairGenerator().generateKeyPair();
    }

    public static EdDSANamedCurveSpec getCurveSpec(){
        return EdDSANamedCurveTable.getByName("ed25519-sha-512");
    }

    public static String PubString(PublicKey pub){
        //return new String(Base64.encodeBase64(Ed25519.PubBytes(pub)));
        //return Base64.encodeToString(Ed25519.PubBytes(pub), Base64.DEFAULT);
        return BaseEncoding.base64().encode(Ed25519.PubBytes(pub));
    }

    public static String PrivateString(PrivateKey priv){
        //return new String(Base64.encodeBase64(Ed25519.PrivateBytes(priv)));
        //return Base64.encodeToString(Ed25519.PrivateBytes(priv), Base64.DEFAULT);
        return BaseEncoding.base64().encode(Ed25519.PrivateBytes(priv));
    }

    public static PublicKey StringToPub(String pub){
        //return Ed25519.BytesToPub(Base64.decodeBase64(pub));
        //return Ed25519.BytesToPub(Base64.decode(pub, Base64.DEFAULT));
        return Ed25519.BytesToPub(BaseEncoding.base64().decode(pub));
    }

    public static PrivateKey StringToPrivate(String priv){
        //return Ed25519.BytesToPrivate(Base64.decodeBase64(priv));
        //return Ed25519.BytesToPrivate(Base64.decode(priv, Base64.DEFAULT));
        return Ed25519.BytesToPrivate(BaseEncoding.base64().decode(priv));
    }

    public static byte[] PubBytes(PublicKey pub){
        return ((EdDSAPublicKey)pub).getAbyte();
    }

    public static byte[] PrivateBytes(PrivateKey priv){
        return ((EdDSAPrivateKey)priv).getSeed();
    }

    public static PublicKey BytesToPub(byte[] pub){
        EdDSAPublicKeySpec pubSpec = new EdDSAPublicKeySpec(pub, Ed25519.getCurveSpec());
        return new EdDSAPublicKey(pubSpec);
    }

    public static PrivateKey BytesToPrivate(byte[] seed){
        EdDSAPrivateKeySpec privSpec = new EdDSAPrivateKeySpec(seed, Ed25519.getCurveSpec());
        return new EdDSAPrivateKey(privSpec);
    }
}
