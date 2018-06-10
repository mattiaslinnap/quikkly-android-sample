package net.quikkly.android.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.quikkly.android.Quikkly;
import net.quikkly.core.QuikklyCore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Quikkly.isConfigured()) {
            Quikkly.configureInstance(MainActivity.this, 2, 0);
            //Quikkly.configureInstance(MainActivity.this, "blueprint_custom.json", 2, 0);
        }
        QuikklyCore.checkLinking();

        findViewById(R.id.scan_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanCameraActivity.launch(MainActivity.this, 1.0, true, false);
                //ScanCameraActivity.launch(MainActivity.this, 2, true, true);
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
