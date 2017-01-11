package ch.epfl.dedis.net;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ExecutionException;

import ch.epfl.dedis.api.Request;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HTTPTest {

    private final Request mRequest = new Request() {
        @Override
        public void callback(String result) {}
        @Override
        public void callbackError(int error) {}
    };

    private final Cothority mCothority = new Cothority("", "");

    @Test
    public void return400ErrorCodeForInexistantAddress() throws ExecutionException, InterruptedException {
        HTTP http = new HTTP(mRequest, mCothority, "", "");
        String response = http.doInBackground();

        assertEquals("", response);
        assertEquals(400, http.getResponseCode());
    }
}
