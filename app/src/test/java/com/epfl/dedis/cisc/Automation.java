package com.epfl.dedis.cisc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.epfl.dedis.net.ConfigUpdate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

// TODO MORE AUTOMATION!
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class Automation {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";
    private static final String ID = "[93,43,247,157,208,67,177,245,85,103,109,163,223,34,254,68,197,111,195,123,223,191,145,244,238,192,32,236,130,72,103,56]";
    private static final String FOO = "[1, 2, 3]";

    private static String sucConnection;
    private static String errNotFound;

    private Gson gson;

    @Before
    public void setup() {
        Application app = RuntimeEnvironment.application;
        sucConnection = app.getResources().getString(R.string.suc_connection);
        errNotFound = app.getResources().getString(R.string.err_not_found);
        gson = new GsonBuilder().serializeNulls().create();
    }

    @Test
    public void createActivityAddIdentity() {
        CreateActivity createActivity = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences preferences = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        assertNotNull(preferences);

        createActivity.setHost(HOST);
        createActivity.setPort(PORT);
        createActivity.setData(FOO);

        createActivity.sendAddIdentity();

        String id = preferences.getString("ID", "");
        assertNotEquals("", id);

        int[] idArray = gson.fromJson(id, int[].class);
        assertEquals(32, idArray.length);
    }

    @Test
    public void mainActivityConfigUpdateInexistentIdentity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        assertNotNull(pref);
        assertNotNull(editor);

        editor.putString("HOST", HOST);
        editor.putString("PORT", PORT);
        editor.putString("ID", FOO);
        editor.apply();

        FloatingActionButton refreshButton = (FloatingActionButton) mainActivity.findViewById(R.id.main_refresh_button);
        assertNotNull(refreshButton);
        refreshButton.performClick();

        TextView textView = (TextView) mainActivity.findViewById(R.id.main_status_value);
        assertNotNull(textView);
        assertEquals(errNotFound, textView.getText().toString());
    }

    @Test
    public void mainActivityConfigUpdateExistentIdentity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        assertNotNull(pref);
        assertNotNull(editor);

        editor.putString("HOST", HOST);
        editor.putString("PORT", PORT);
        editor.putString("ID", ID);
        editor.apply();

        mainActivity.sendConfigUpdate();

        TextView textView = (TextView) mainActivity.findViewById(R.id.main_status_value);
        assertEquals(sucConnection, textView.getText().toString());
    }

    @Test
    public void joinActivityConfigUpdateOnInexistentIdentity() throws Exception {
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);

        ja.setHost(HOST);
        ja.setPort(PORT);
        ja.setData(FOO);
        ja.setId(FOO);

        ja.sendConfigUpdate();
        String cuJSON = pref.getString("LATEST", "");
        assertEquals("", cuJSON);
    }

    @Test
    public void joinActivityConfigUpdateOnExistentIdentity() throws Exception {
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);

        ja.setHost(HOST);
        ja.setPort(PORT);
        ja.setData(FOO);
        ja.setId(ID);
        
        ja.sendConfigUpdate();
        String cuJSON = pref.getString("LATEST", "");
        assertNotEquals("", cuJSON);

        gson.fromJson(cuJSON, ConfigUpdate.class);
    }

    @Test
    public void joinActivityProposeSend() throws Exception{
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);

        ja.setHost(HOST);
        ja.setPort(PORT);
        ja.setStage(0);

        int[] id = gson.fromJson(ID, int[].class);
        Map<String, int[]> device = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        com.epfl.dedis.net.Config config = new com.epfl.dedis.net.Config(3, device, data);

        String psJSON = gson.toJson(new ConfigUpdate(id, config));
        ja.sendProposeSend(psJSON);

        psJSON = pref.getString("PROPOSED", "");
        assertNotEquals("", psJSON);

        gson.fromJson(psJSON, ConfigUpdate.class);
    }
}