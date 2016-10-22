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
import com.google.gson.annotations.SerializedName;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class JoinActivity extends AppCompatActivity implements Activity, ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private Identity mIdentity;
    private boolean mProposed;

    private class QRMessage {
        @SerializedName("ID")
        String id;

        @SerializedName("Host")
        String host;

        @SerializedName("Port")
        String port;
    }

    // TODO: Remove UI blocking after successful scan
    public void taskJoin() {
        if (!mProposed) {
            mIdentity.newDevice(Utils.uuid());
            new ProposeSend(this, mIdentity);
            mProposed = true;
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
            editor.putString(IDENTITY, Utils.toJson(mIdentity));
            editor.apply();

            Intent intent = new Intent(JoinActivity.this, ConfigActivity.class);
            intent.putExtra("wait", "Wait for joining approval.");
            intent.putExtra("pro", false);

            startActivity(intent);
            finish();
        }
    }

    public void taskFail(int error) {
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
        String json = rawResult.getText();
        QRMessage qrm = Utils.fromJson(json, QRMessage.class);

        mIdentity = new Identity(new Cothority(qrm.host, qrm.port), Utils.decodeBase64(qrm.id));
        new ConfigUpdate(JoinActivity.this, mIdentity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }
}