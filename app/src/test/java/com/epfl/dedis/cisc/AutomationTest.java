package com.epfl.dedis.cisc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
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
    private static final String ID = "[34,94,216,126,2,57,11,179,238,53,227,38,154,237,184,156,147,238,96,135,87,166,60,237,242,17,128,239,252,48,207,247]";
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

        EditText hostEditText = (EditText) createActivity.findViewById(R.id.host_editText);
        assertNotNull(hostEditText);
        hostEditText.setText(HOST);

        EditText portEditText = (EditText) createActivity.findViewById(R.id.port_editText);
        assertNotNull(portEditText);
        portEditText.setText(PORT);

        EditText dataEditText = (EditText) createActivity.findViewById(R.id.data_editText);
        assertNotNull(dataEditText);
        dataEditText.setText(FOO);

        Button button = (Button) createActivity.findViewById(R.id.create_button);
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

        FloatingActionButton refreshButton = (FloatingActionButton) mainActivity.findViewById(R.id.refresh_button);
        assertNotNull(refreshButton);
        refreshButton.performClick();

        TextView textView = (TextView) mainActivity.findViewById(R.id.status_value);
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

        FloatingActionButton refreshButton = (FloatingActionButton) mainActivity.findViewById(R.id.refresh_button);
        assertNotNull(refreshButton);
        refreshButton.performClick();

        TextView textView = (TextView) mainActivity.findViewById(R.id.status_value);
        assertEquals(sucConnection, textView.getText().toString());
    }

    @Test
    public void joinConfigUpdate() throws Exception {
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);

        EditText hostEditText = (EditText) ja.findViewById(R.id.host_editText);
        assertNotNull(hostEditText);
        hostEditText.setText(HOST);

        EditText portEditText = (EditText) ja.findViewById(R.id.port_editText);
        assertNotNull(portEditText);
        portEditText.setText(PORT);

        EditText dataEditText = (EditText) ja.findViewById(R.id.data_editText);
        assertNotNull(dataEditText);
        dataEditText.setText(FOO);

        EditText idEditText = (EditText) ja.findViewById(R.id.id_editText);
        assertNotNull(idEditText);
        idEditText.setText(ID);
        
        Button joinButton = (Button) ja.findViewById(R.id.join_join_button);
        assertNotNull(joinButton);
        joinButton.performClick();
    }
}