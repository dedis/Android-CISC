package com.epfl.dedis.cisc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AutomationTest {

    private static final String HOST = "localhost";
    private static final String PORT = "2000";
    private static final String ID = "[129,89,159,161,232,219,88,154,98,161,29,73,211,47,193,115,52,72,192,131,88,29,35,37,106,172,49,19,189,145,31,143]";
    private static final String FOO = "[1, 2, 3]";

    private static String sucConnection;
    private static String errNotFound;

    private Gson gson;

    @Before
    public void setup() {
        Application app = RuntimeEnvironment.application;
        sucConnection = app.getResources().getString(R.string.suc_connection);
        errNotFound = app.getResources().getString(R.string.err_not_found);
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @Test
    public void addIdentityToCothority() throws Exception {
        CreateActivity createActivity = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences preferences = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        assertNotNull(preferences);

        EditText hostEditText = (EditText) createActivity.findViewById(R.id.create_host_edit);
        assertNotNull(hostEditText);
        hostEditText.setText(HOST);

        EditText portEditText = (EditText) createActivity.findViewById(R.id.create_port_edit);
        assertNotNull(portEditText);
        portEditText.setText(PORT);

        EditText dataEditText = (EditText) createActivity.findViewById(R.id.create_data_edit);
        assertNotNull(dataEditText);
        dataEditText.setText(FOO);

        FloatingActionButton button = (FloatingActionButton) createActivity.findViewById(R.id.create_create_button);
        assertNotNull(button);
        button.performClick();

        String id = preferences.getString("ID", "");
        assertNotEquals("", id);

        int[] idArray = gson.fromJson(id, int[].class);
        assertEquals(32, idArray.length);
    }

    @Test
    public void configUpdateOnInexistentIdentity() throws Exception {
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
    public void configUpdateOnExistentIdentity() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        assertNotNull(pref);
        assertNotNull(editor);

        editor.putString("HOST", HOST);
        editor.putString("PORT", PORT);
        editor.putString("ID", ID);
        editor.apply();

        FloatingActionButton refreshButton = (FloatingActionButton) mainActivity.findViewById(R.id.main_refresh_button);
        assertNotNull(refreshButton);
        refreshButton.performClick();

        TextView textView = (TextView) mainActivity.findViewById(R.id.main_status_value);
        assertEquals(sucConnection, textView.getText().toString());
    }

    @Test
    public void joinConfigUpdate() throws Exception {
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);

        EditText hostEditText = (EditText) ja.findViewById(R.id.join_host_edit);
        assertNotNull(hostEditText);
        hostEditText.setText(HOST);

        EditText portEditText = (EditText) ja.findViewById(R.id.join_port_edit);
        assertNotNull(portEditText);
        portEditText.setText(PORT);

        EditText dataEditText = (EditText) ja.findViewById(R.id.join_data_edit);
        assertNotNull(dataEditText);
        dataEditText.setText(FOO);

        EditText idEditText = (EditText) ja.findViewById(R.id.join_identity_edit);
        assertNotNull(idEditText);
        idEditText.setText(ID);
        
        FloatingActionButton joinButton = (FloatingActionButton) ja.findViewById(R.id.join_join_button);
        assertNotNull(joinButton);
        joinButton.performClick();
    }
}