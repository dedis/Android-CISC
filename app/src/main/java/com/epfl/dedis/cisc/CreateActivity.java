package com.epfl.dedis.cisc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {

    private EditText mHostEditText;
    private EditText mPortEditText;
    private EditText mDataEditText;

    private InputMethodManager imm;

    private void clear() {
        mHostEditText.setText("");
        mPortEditText.setText("");
        mDataEditText.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mHostEditText = (EditText) findViewById(R.id.host_editText);
        assert mHostEditText != null;
        mHostEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mHostEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mHostEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        mPortEditText = (EditText) findViewById(R.id.port_editText);
        assert mPortEditText != null;
        mPortEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mPortEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPortEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        mDataEditText = (EditText) findViewById(R.id.data_editText);
        assert mPortEditText != null;
        mDataEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mDataEditText.clearFocus();
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDataEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        Button mClearButton = (Button) findViewById(R.id.clear_button);
        assert mClearButton != null;
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clear();
            }
        });

        Button mCreateButton = (Button) findViewById(R.id.create_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("START NEW SKIPCHAIN!");
            }
        });
    }
}
