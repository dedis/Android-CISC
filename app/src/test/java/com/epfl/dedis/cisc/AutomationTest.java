package com.epfl.dedis.cisc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.epfl.dedis.net.HTTP;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AutomationTest {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";

    private static String sucConnection;
    private static String errNotFound;

    @Before
    public void setup() {
        Application app = RuntimeEnvironment.application;
        sucConnection = app.getResources().getString(R.string.suc_connection);
        errNotFound = app.getResources().getString(R.string.err_not_found);
    }

    @Test
    public void addIdentityToCothority() throws IOException {
        CreateActivity ca = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);

        new HTTP(ca).execute(HOST, PORT, "ai", ca.makeJson());
        assertNotNull(pref);

        String idString = pref.getString("ID", "foo");
        assertNotEquals(idString, "foo");

        int[] id = new Gson().fromJson(idString, int[].class);
        assertEquals(id.length, 32);
    }

    @Test
    public void configUpdateOnInexistentIdentity() throws Exception {
        CreateActivity ca = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        new HTTP(ca).execute(HOST, PORT, "ai", ca.makeJson());

        String idString = pref.getString("ID", "foo");
        assertNotEquals(idString, "foo");

        MainActivity ma = Robolectric.setupActivity(MainActivity.class);
        ma.setId("[1]");

        new HTTP(ma).execute(HOST, PORT, "cu", ma.makeJson());

        View view = ma.findViewById(R.id.status_value);
        assertNotNull(view);

        TextView textView = (TextView) view;
        String status = textView.getText().toString();
        assertEquals(status, errNotFound);
    }

    @Test
    public void configUpdateOnExistentIdentity() throws Exception {
        CreateActivity ca = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        new HTTP(ca).execute(HOST, PORT, "ai", ca.makeJson());

        String idString = pref.getString("ID", "foo");
        assertNotEquals(idString, "foo");

        MainActivity ma = Robolectric.setupActivity(MainActivity.class);
        ma.setId(idString);

        new HTTP(ma).execute(HOST, PORT, "cu", ma.makeJson());

        View view = ma.findViewById(R.id.status_value);
        assertNotNull(view);

        TextView textView = (TextView) view;
        String status = textView.getText().toString();
        assertEquals(status, sucConnection);
    }
}