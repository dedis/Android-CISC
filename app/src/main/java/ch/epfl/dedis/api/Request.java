package ch.epfl.dedis.api;

public interface Request {
    void callback(String result);
    void callbackError(int error);
}
