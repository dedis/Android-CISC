package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.ProposeSend;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Cothority;
import com.epfl.dedis.net.Identity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements Activity, ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private Identity identity;

    private boolean proposed;

    public void callbackSuccess() {
        if (!proposed) {
            identity.newDevice("test");
            new ProposeSend(this, identity);
            proposed = true;
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
            editor.putString(IDENTITY, Utils.toJson(identity));
            editor.apply();

            Intent intent = new Intent(ScannerActivity.this, ConfigActivity.class);
            startActivity(intent);
        }
    }

    public void callbackError(int error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();

        String[] json = Utils.fromJson(result, String[].class);
        byte[] bla = Utils.fromJson(json[0], byte[].class);
        identity = new Identity(new Cothority(json[1], json[2]), bla);
        new ConfigUpdate(ScannerActivity.this, identity);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }
}
