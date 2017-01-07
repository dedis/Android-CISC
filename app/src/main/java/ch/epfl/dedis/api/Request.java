package ch.epfl.dedis.api;

/**
 * Proxy interface to handle message emissions from Activity to
 * the HTTP handler. It stimulates a callback to the calling
 * Activity after the communication procedure ends.
 *
 * @author Andrea Caforio
 */
public interface Request {

    /**
     * A succesful HTTP interaction is marked hereby.
     *
     * @param result response String from the Cothority
     */
    void callback(String result);

    /**
     * In case of an error within the communication channel
     * an error is signalled.
     *
     * @param error code
     */
    void callbackError(int error);
}
