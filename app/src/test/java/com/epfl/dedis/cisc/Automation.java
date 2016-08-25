package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.epfl.dedis.net.Replies;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.CreateIdentity;
import com.epfl.dedis.net.ConfigUpdate;
import com.epfl.dedis.net.Identity;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

// TODO MORE AUTOMATION!
@RunWith(JUnit4.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class Automation {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";
    private static final String ID = "[164,219,221,206,80,65,140,159,45,43,12,72,87,108,103,196,156,211,180,26,221,181,126,238,70,68,220,102,0,73,103,200]";
    private static final String FOO = "[1, 2, 3]";

    private static String sucConnection;
    private static String errNotFound;

    private Identity identity;

    private Gson gson;
    private Replies replies;

    @Before
    public void setup() throws Exception {
//        Application app = RuntimeEnvironment.application;
//        sucConnection = app.getResources().getString(R.string.suc_connection);
//        errNotFound = app.getResources().getString(R.string.err_not_found);
//        gson = new GsonBuilder().serializeNulls().create();
        replies = new Replies(){
            public void callbackSuccess(String result){
                System.out.println(result);
            }
            public void callbackError(int error){
                Assert.fail("Error while contacting cothority");
            }
        };

        Cothority cot = new Cothority(HOST, PORT);
        CreateIdentity id = new CreateIdentity(replies, cot, true);
        identity = id.getIdentity();
    }

    @Test
    public void createActivityAddIdentity(){
        assertEquals("test", identity.getDeviceName());
        assertEquals(identity.getPub(), identity.getConfig().getDevices().get("test"));
    }

    @Test
    public void mainActivityConfigUpdateInexistentIdentity() {
//        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
//        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        assertNotNull(pref);
//        assertNotNull(editor);
//
//        editor.putString("HOST", HOST);
//        editor.putString("PORT", PORT);
//        editor.putString("ID", FOO);
//        editor.apply();
//
//        FloatingActionButton refreshButton = (FloatingActionButton) mainActivity.findViewById(R.id.main_refresh_button);
//        assertNotNull(refreshButton);
//        refreshButton.performClick();
//
//        TextView textView = (TextView) mainActivity.findViewById(R.id.main_status_value);
//        assertNotNull(textView);
//        assertEquals(errNotFound, textView.getText().toString());
    }

    @Test
    public void mainActivityConfigUpdateExistingIdentity() {
//        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
//        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        assertNotNull(pref);
//        assertNotNull(editor);
//
//        editor.putString("HOST", HOST);
//        editor.putString("PORT", PORT);
//        editor.putString("ID", ID);
//        editor.apply();
//
//        mainActivity.sendConfigUpdate();
//
//        TextView textView = (TextView) mainActivity.findViewById(R.id.main_status_value);
//        assertEquals(sucConnection, textView.getText().toString());
        System.out.println(Arrays.toString(identity.getSkipchainId()));
        ConfigUpdate cu = new ConfigUpdate(replies, identity, true);
        Identity id2 = cu.getIdentity();
        assertEquals(identity.getPub(), id2.getPub());
        assertEquals(identity.getSkipchainId(), id2.getSkipchainId());
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
//        com.epfl.dedis.net.Config config = new com.epfl.dedis.net.Config(3, "Motorola");
//
//        String psJSON = gson.toJson(new ConfigUpdate(id, config));
//        ja.sendProposeSend(psJSON);
//
//        psJSON = pref.getString("PROPOSED", "");
//        assertNotEquals("", psJSON);
//
//        gson.fromJson(psJSON, ConfigUpdate.class);
    }
}