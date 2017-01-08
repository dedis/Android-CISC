package ch.epfl.dedis.crypto;

import com.google.gson.annotations.SerializedName;

/**
 * The QR stamp class is used to serizalize its fields into a
 * JSON string before using the representation for the QR-code in
 * the main activity.
 *
 * @author Andrea Caforio
 */
public class QRStamp {

    @SerializedName("ID")
    final String mId;

    @SerializedName("Host")
    final String mHost;

    @SerializedName("Port")
    final String mPort;

    public QRStamp(String id, String host, String port) {
        this.mId = id;
        this.mHost = host;
        this.mPort = port;
    }

    public String getId() {
        return mId;
    }

    public String getHost() {
        return mHost;
    }

    public String getPort() {
        return mPort;
    }
}
