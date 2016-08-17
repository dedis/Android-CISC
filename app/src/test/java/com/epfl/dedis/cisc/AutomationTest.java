package com.epfl.dedis.cisc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
    private static final String ID = "[87,166,33,239,179,177,174,7,13,89,52,146,240,201,26,173,87,76,4,214,237,237,86,173,142,159,212,167,62,217,77,219]";

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
        CreateActivity ca = Robolectric.setupActivity(CreateActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);

        assertNotNull(pref);

        EditText hostEditText = (EditText) ca.findViewById(R.id.host_editText);
        hostEditText.setText(HOST);

        EditText portEditText = (EditText) ca.findViewById(R.id.port_editText);
        portEditText.setText(PORT);

        EditText dataEditText = (EditText) ca.findViewById(R.id.data_editText);
        dataEditText.setText(FOO);

        Button button = (Button) ca.findViewById(R.id.create_button);
        assertNotNull(button);
        button.performClick();

        String id = pref.getString("ID", "");
        assertNotEquals("", id);

        int[] idArray = gson.fromJson(id, int[].class);
        assertEquals(32, idArray.length);
    }

    @Test
    public void configUpdateOnInexistentIdentity() throws Exception {
        MainActivity ma = Robolectric.setupActivity(MainActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        assertNotNull(pref);
        assertNotNull(editor);

        editor.putString("HOST", HOST);
        editor.putString("PORT", PORT);
        editor.putString("ID", FOO);
        editor.apply();

        ma.update();

        TextView textView = (TextView) ma.findViewById(R.id.status_value);
        assertNotNull(textView);
        assertEquals(errNotFound, textView.getText().toString());
    }

    @Test
    public void configUpdateOnExistentIdentity() throws Exception {
        MainActivity ma = Robolectric.setupActivity(MainActivity.class);
        SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("LOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        assertNotNull(pref);
        assertNotNull(editor);

        editor.putString("HOST", HOST);
        editor.putString("PORT", PORT);
        editor.putString("ID", ID);
        editor.apply();

        ma.update();

        TextView textView = (TextView) ma.findViewById(R.id.status_value);
        assertEquals(sucConnection, textView.getText().toString());
    }

    @Test
    public void joinConfigUpdate() throws Exception {
        JoinActivity ja = Robolectric.setupActivity(JoinActivity.class);

        EditText host = (EditText) ja.findViewById(R.id.host_editText);
        host.setText(HOST);

        EditText port = (EditText) ja.findViewById(R.id.port_editText);
        port.setText(PORT);

        EditText data = (EditText) ja.findViewById(R.id.data_editText);
        data.setText(FOO);

        EditText id = (EditText) ja.findViewById(R.id.id_editText);
        id.setText(ID);
        Button button = (Button) ja.findViewById(R.id.join_join_button);
        button.performClick();
    }
}