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
import com.epfl.dedis.crypto.QRStamp;
import com.google.gson.annotations.SerializedName;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.epfl.dedis.cisc.JoinActivity.JoinState.CONF;
import static com.epfl.dedis.cisc.JoinActivity.JoinState.PROP;

public class JoinActivity extends AppCompatActivity implements Activity, ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private Identity mIdentity;
    private JoinState mJoinState;

    private class QRMessage {
        @SerializedName("ID")
        String id;

        @SerializedName("Host")
        String host;

        @SerializedName("Port")
        String port;
    }

    public enum JoinState {
        CONF, PROP
    }

    public void taskJoin() {
        if (mJoinState == CONF) {
            mIdentity.newDevice(Utils.uuid());
            new ProposeSend(this, mIdentity);
            mJoinState = PROP;
        } else if (mJoinState == PROP) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF, Context.MODE_PRIVATE).edit();
            editor.putString(IDENTITY, Utils.toJson(mIdentity));
            editor.apply();

            Intent intent = new Intent(JoinActivity.this, ConfigActivity.class);
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
        QRStamp qrs = Utils.fromJson(json, QRStamp.class);
        Cothority cothority = new Cothority(qrs.getHost(), qrs.getPort());

        mIdentity = new Identity(cothority, Utils.decodeBase64(qrs.getId()), ConfigActivity.ConfigState.PROP);
        new ConfigUpdate(JoinActivity.this, mIdentity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        mJoinState = CONF;
    }
}