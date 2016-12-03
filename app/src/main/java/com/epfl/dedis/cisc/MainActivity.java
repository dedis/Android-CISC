package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.api.GetUpdateChain;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;
import com.google.zxing.WriterException;

import static com.epfl.dedis.cisc.MainActivity.MainState.CONNECTION;
import static com.epfl.dedis.cisc.MainActivity.MainState.VERIFICATION;

public class MainActivity extends AppCompatActivity implements Activity {

    private ImageView mQrImageView;
    private TextView mStatusLabel;

    private Identity mIdentity;

    private MainState mMainState;

    public enum MainState {
        CONNECTION, VERIFICATION
    }

    public void taskJoin() {
        if (mMainState == CONNECTION) {
            float px = Utils.dpToPixel(mQrImageView.getWidth(), getResources().getDisplayMetrics());
            String identityBase64 = Utils.encodeBase64(mIdentity.getId());

            try {
                mQrImageView.setImageBitmap(Utils.encodeQR(identityBase64, (int) px));
                mStatusLabel.setText(R.string.info_connection);
            } catch (WriterException e) {
                mStatusLabel.setText("Not connected.");
            }
        } else if (mMainState == VERIFICATION) {
            Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void taskFail(int error) {
        mStatusLabel.setText(error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusLabel = (TextView) findViewById(R.id.main_status_label);
        mStatusLabel.setText("Not connected");

        mQrImageView = (ImageView) findViewById(R.id.main_qr_image);
        assert mQrImageView != null;
        mQrImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.main_join_button);
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton mRefreshButton = (FloatingActionButton) findViewById(R.id.main_refresh_button);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
                String json = pref.getString(IDENTITY, "");
                if (!json.isEmpty()) {
                    mMainState = CONNECTION;
                    mIdentity = Utils.fromJson(json, Identity.class);
                    new ConfigUpdate(MainActivity.this, mIdentity);
                }
            }
        });

        FloatingActionButton mInfoButton = (FloatingActionButton) findViewById(R.id.main_info_button);
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
                String json = pref.getString(IDENTITY, "");
                if (!json.isEmpty()) {
                    mMainState = VERIFICATION;
                    mIdentity = Utils.fromJson(json, Identity.class);
                    new GetUpdateChain(MainActivity.this, mIdentity);
                }
            }
        });
    }
}
