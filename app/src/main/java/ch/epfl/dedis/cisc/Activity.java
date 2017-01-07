package ch.epfl.dedis.cisc;

/**
 * The Activity interface unifies all UI-classes by restricting them
 * to callback methods which are used to update the GUI after a network
 * request.
 *
 * @author Andrea Caforio
 */
public interface Activity {
    String PREF = "PREFERENCES";
    String IDENTITY = "IDENTITY";

    int PERMISSION_CAMERA = 0;

    /**
     * Signifies a successful network interaction by joining the
     * network thread with the UI-process. This method is always called
     * by a request class.
     */
    void taskJoin();

    /**
     * In case the request class passed error codes on to the Activity
     * which then takes appropriate measures.
     *
     * @param error code
     */
    void taskFail(int error);
}