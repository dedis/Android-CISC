package ch.epfl.dedis.cisc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.dedis.api.ConfigUpdate;
import ch.epfl.dedis.api.GetUpdateChain;
import ch.epfl.dedis.crypto.QRStamp;
import ch.epfl.dedis.crypto.Utils;
import ch.epfl.dedis.net.Identity;
import com.google.zxing.WriterException;

import static ch.epfl.dedis.cisc.MainActivity.MainState.CONNECTION;
import static ch.epfl.dedis.cisc.MainActivity.MainState.VERIFICATION;

/**
 * Entry point of the application. This Activity displays the QR-code
 * that can be used to join an existing Skipchain. Furthermore via the
 * MainActivity the connection can be probed by sending a ConfigUpdate.
 * More importantly, the Skipchain verification is perfomed here.
 *
 * @author Andrea Caforio
 */
public class MainActivity extends AppCompatActivity implements Activity {

    private static final String TAG = "cisc.MainActivity";

    private ImageView mQrImageView;
    private TextView mStatusLabel;

    private Identity mIdentity;
    private MainState mMainState;

    /**
     * State transitions corresponding to operation that has been
     * performed.
     */
    public void taskJoin() {
        Log.d(TAG, "Task join: " + mMainState.name());
        if (mMainState == CONNECTION) {
            float px = Utils.dpToPixel(mQrImageView.getWidth(), getResources().getDisplayMetrics());

            String identityBase64 = Utils.encodeBase64(mIdentity.getId());
            String host = mIdentity.getCothority().getHost();
            String port = mIdentity.getCothority().getPort();
            String jsonStamp = Utils.toJson(new QRStamp(identityBase64, host, port));

            try {
                // Only display QR-code when connection is alive.
                mQrImageView.setImageBitmap(Utils.encodeQR(jsonStamp, (int)px));
                mStatusLabel.setText(R.string.info_connection);
            } catch (WriterException e) {
                mStatusLabel.setText(R.string.info_noconnection);
            }
        } else if (mMainState == VERIFICATION) {
            Toast.makeText(this, R.string.info_verification, Toast.LENGTH_SHORT).show();
        }
    }

    public void taskFail(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate called.");

        // Request camera permission if needed.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }

        mStatusLabel = (TextView) findViewById(R.id.main_status_label);
        mStatusLabel.setText(R.string.info_noconnection);

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
                // Only start camera if permission has been granted.
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this, R.string.info_enablecamera, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                    startActivity(intent);
                }
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

    /**
     * Handle result of the permission requests.
     *
     * @param requestCode indicator of permission type
     * @param permissions names of requested permissions
     * @param grantResults indicator if requested permission was granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.info_scannerready, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.info_scannernotready, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Keywords for the marking the current state of the MainActivity.
     *
     * CONNECTION:      State after sending a ConfigUpdate
     * VERIFICATION:    State after performing a GetUpdateChain for verification
     */
    public enum MainState {
        CONNECTION, VERIFICATION
    }
}
