package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.dedis.api.ConfigUpdate;
import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Identity;
import com.google.zxing.WriterException;

public class MainActivity extends AppCompatActivity implements Activity {

    private ImageView mQrImage;
    private TextView mStatusLabel;

    private Identity identity;

    public void taskJoin() {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mQrImage.getWidth(), r.getDisplayMetrics());
        String identityBase64 = Utils.encodeBase64(identity.getId());

        try {
            mQrImage.setImageBitmap(Utils.encodeQR(identityBase64, (int) px));
            mStatusLabel.setText(R.string.suc_connection);
            System.out.println(mStatusLabel.getText());
        } catch (WriterException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void taskFail(int error) {
        mStatusLabel.setText(error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQrImage = (ImageView) findViewById(R.id.main_qr_image);
        assert mQrImage != null;
        mQrImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                intent.putExtra(STATUS_INTENT, mStatusLabel.getText());
                startActivity(intent);
            }
        });

        mStatusLabel = (TextView) findViewById(R.id.main_status_label);
        assert mStatusLabel != null;

        FloatingActionButton mCreateButton = (FloatingActionButton) findViewById(R.id.main_create_button);
        assert mCreateButton != null;
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton mJoinButton = (FloatingActionButton) findViewById(R.id.main_join_button);
        assert mJoinButton != null;
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
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
