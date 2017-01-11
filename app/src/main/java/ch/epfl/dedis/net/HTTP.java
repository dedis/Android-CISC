package ch.epfl.dedis.net;

import android.os.AsyncTask;
import android.util.Log;

import ch.epfl.dedis.api.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The HTTP class is the network socket of this project. It handles all
 * communication with the Cothority service through the standard HTTP-
 * protocol. The socket is fully concurrent to avoid any blocking of the
 * main thread.
 *
 * @author Andrea Caforio
 */
public class HTTP extends AsyncTask<Void, Void, String> {

    private static final String TAG = "net.HTTP";

    private static final int TIMEOUT = 2000;
    private static final int BUF_SIZE = 10000;

    private final Request mRequest;
    private final Cothority mCothority;
    private final String mPath;
    private final String mJson;

    private int mResponseCode;

    public HTTP(Request request, Cothority cothority, String path, String json) {
        mRequest = request;
        mCothority = cothority;
        mPath = path;
        mJson = json;
    }

    /**
     * Spawns a new thread to deal with the network traffic. All acknowledgements
     * from the Cothority are parsed to Strings.
     *
     * @param params Unused in this context.
     * @return Acknowledgement message from the Cothority.
     */
    @Override
    public String doInBackground(Void... params) {
        try {
            Log.d(TAG, "Send " + mPath + ": " + mJson);
            URL url = new URL("http://" + mCothority.getHost() + ":" + mCothority.getPort() + "/" + mPath);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(TIMEOUT);
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            OutputStream out = http.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(mJson);
            writer.flush();
            writer.close();

            mResponseCode = http.getResponseCode();
            if (mResponseCode != 200) {
                Log.e(TAG, http.getResponseCode() + " " + http.getResponseMessage());
                http.disconnect();
                return "";
            }

            InputStreamReader in = new InputStreamReader(http.getInputStream());
            BufferedReader br = new BufferedReader(in);

            char[] chars = new char[BUF_SIZE];
            int size = br.read(chars);

            String response = new String(chars).substring(0, size);
            Log.d(TAG, "Received: " + response);
            http.disconnect();
            return response;
        } catch (IOException e) {
            mResponseCode = 400;
            return "";
        }
    }

    /**
     * Joins the thread spawned by doInBackground and passes the
     * result to the calling activity.
     *
     * @param result Acknowledgement from doInBackground.
     */
    @Override
    public void onPostExecute(String result) {
        if (result.isEmpty()) {
            mRequest.callbackError(mResponseCode);
        } else {
            mRequest.callback(result);
        }
    }

    public int getResponseCode() {
        return mResponseCode;
    }
}
