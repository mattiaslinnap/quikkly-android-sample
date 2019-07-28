package net.quikkly.android.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.ScanActivity;
import net.quikkly.android.ui.ScanFragment;
import net.quikkly.core.QuikklyCore;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(Quikkly.TAG, "Linking test");
        QuikklyCore.checkLinking();
        Log.e(Quikkly.TAG, "Linking OK");

        if (!Quikkly.isConfigured()) {
            Quikkly.configureInstance(MainActivity.this, 2, 0);
            // Quikkly.configureInstance(this, "blueprint_custom.json", 2, 0);
        }

        findViewById(R.id.scan_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.launch(MainActivity.this, 1, true, false, ScanFragment.CAMERA_PREVIEW_FIT_NONE);
                //ScanActivity.launch(MainActivity.this, 2, true, true, ScanFragment.CAMERA_PREVIEW_FIT_NONE);
            }
        });

        findViewById(R.id.scan_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanImageActivity.launch(MainActivity.this);
            }
        });

        findViewById(R.id.gen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RenderActivity.launch(MainActivity.this);
            }
        });

        ((TextView)findViewById(R.id.version)).setText("Core lib version " + Quikkly.versionString() + ", debug=" + QuikklyCore.isDebug());
    }

}
