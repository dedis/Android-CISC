package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;

public class MainActivity extends AppCompatActivity implements Activity {

    private ImageView mQrImage;

    private Identity identity;

    public void callbackSuccess() {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mQrImage.getWidth(), r.getDisplayMetrics());
        String identityBase64 = Utils.encodeBase64(identity.getId());

        mQrImage.setImageBitmap(Utils.encodeQR(identityBase64, (int) px));
        mQrImage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSuccess));
    }

    public void callbackError(int error) {
        mQrImage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorFailure));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQrImage = (ImageView) findViewById(R.id.main_qr_image);
        assert mQrImage != null;
        mQrImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.main_create_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.main_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton mRefreshButton = (FloatingActionButton) findViewById(R.id.main_refresh_button);
        assert mRefreshButton != null;
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
                String json = pref.getString(IDENTITY, "");
                if (!json.isEmpty()) {
                    identity = Utils.fromJson(json, Identity.class);
                    new ConfigUpdate(MainActivity.this, identity);
                }
            }
        });
    }
}
