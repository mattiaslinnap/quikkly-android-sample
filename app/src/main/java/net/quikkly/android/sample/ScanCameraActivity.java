package net.quikkly.android.sample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ScanResultListener;
import net.quikkly.android.ui.CameraPreview;
import net.quikkly.android.ui.ScanFragment;
import net.quikkly.core.ScanResult;
import net.quikkly.core.Tag;


public class ScanCameraActivity extends AppCompatActivity implements ScanResultListener {

    public static final String EXTRA_SHOW_OVERLAY = "extra_show_overlay";
    public static final String EXTRA_SHOW_STATUS_CODE = "extra_show_status_code";
    public static final String EXTRA_ZOOM = "extra_zoom";

    Handler handler = new Handler();

    /**
     * Helper to start ScanCameraActivity with settings in Intent.
     */
    public static void launch(Context context, double zoom, boolean showResultOverlay, boolean showStatusCode) {
        Intent intent = new Intent(context, ScanCameraActivity.class);
        // Pass settings to onCreate() via Intent.
        intent.putExtra(EXTRA_SHOW_OVERLAY, showResultOverlay);
        intent.putExtra(EXTRA_SHOW_STATUS_CODE, showStatusCode);
        intent.putExtra(EXTRA_ZOOM, zoom);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_scan_camera);

        // Check that Quikkly is set up before other usage.
        Quikkly.getInstance();

        ScanFragment scanFragment = new ScanFragment();

        // Configure ScanFragment
        scanFragment.setShowResultOverlay(getIntent().getBooleanExtra(EXTRA_SHOW_OVERLAY, false));
        scanFragment.setShowStatusCode(getIntent().getBooleanExtra(EXTRA_SHOW_STATUS_CODE, false));
        scanFragment.setZoom(getIntent().getDoubleExtra(EXTRA_ZOOM, CameraPreview.DEFAULT_ZOOM));
        scanFragment.setScanResultListener(this);

        // Insert ScanFragment into this Activity's layout.
        FragmentManager fragmentManager = super.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.scan_camera_activity_root, scanFragment);
        fragmentTransaction.commit();
    }

    /**
     * Do something with ScanResult.
     *
     * Warning: will be called from background threads. Use handlers to post back to UI thread.
     */
    @Override
    public void onScanResult(@Nullable final ScanResult scanResult) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Do something in UI thread with ScanResult.
                if (scanResult != null && !scanResult.isEmpty()) {
                    for (Tag tag : scanResult.tags) {
                        Log.d(Quikkly.TAG, String.format("Scanned code %s:%s", tag.templateIdentifier, tag.getData()));
                    }
                }
            }
        });
    }

}
