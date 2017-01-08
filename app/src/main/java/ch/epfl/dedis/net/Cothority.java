package ch.epfl.dedis.net;

/**
 * The Cothority class bundles the host address and port number
 * of the node the device will be talking to.
 *
 * @author Andrea Caforio
 */
public class Cothority {

    private final String mHost;
    private final String mPort;

    public Cothority(String host, String port){
        mHost = host;
        mPort = port;
    }

    public String getHost() {
        return mHost;
    }

    public String getPort() {
        return mPort;
    }
}
