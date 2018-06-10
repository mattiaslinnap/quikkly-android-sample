package net.quikkly.android.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.quikkly.android.Quikkly;
import net.quikkly.android.ui.ScanResultsOverlay;
import net.quikkly.core.ScanResult;
import net.quikkly.core.Tag;

import java.io.IOException;

public class ScanImageActivity extends AppCompatActivity {

    public static final int PHOTO_REQUEST = 19121;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ScanImageActivity.class));
    }

    RelativeLayout holder;
    ImageView imageView;
    ScanResultsOverlay overlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_image);

        holder = (RelativeLayout)findViewById(R.id.holder);
        imageView = (ImageView)findViewById(R.id.image);
        overlay = (ScanResultsOverlay)findViewById(R.id.overlay);

        findViewById(R.id.pick_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PHOTO_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    if (photo != null) {
                        imageView.setImageBitmap(photo);
                        resizeOverlay();
                        new ScanImageTask().execute(photo);
                    } else {
                        Toast.makeText(this, "Error getting image.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error getting image.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class ScanImageTask extends AsyncTask<Bitmap, Void, ScanResult> {
        @Override
        protected void onPreExecute() {
            overlay.setResult(null);
        }

        protected ScanResult doInBackground(Bitmap... bitmaps) {
            if (bitmaps == null || bitmaps.length == 0)
                return null;
            return Quikkly.getInstance().scanSingleImage(bitmaps[0]);
        }

        protected void onPostExecute(ScanResult result) {
            overlay.setResult(result);
            if (result == null || result.isEmpty()) {
                Toast.makeText(ScanImageActivity.this, "No tags found", Toast.LENGTH_SHORT).show();
            } else {
                for (Tag tag : result.tags) {
                    Toast.makeText(ScanImageActivity.this, "Found " + tag.getData(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Resize overlay so that it matches the actual image, not the entire ImageView.
    private void resizeOverlay() {
        Pair<Integer, Integer> renderedSize = getBitmapSizeInsideImageView(imageView);
        if (renderedSize == null)
            return;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)overlay.getLayoutParams();
        params.width = renderedSize.first;
        params.height = renderedSize.second;
        overlay.setLayoutParams(params);
        overlay.requestLayout();
    }

    public static Pair<Integer, Integer> getBitmapSizeInsideImageView(ImageView imageView) {
        if (imageView == null || imageView.getDrawable() == null)
            return null;

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        return new Pair<>(actW, actH);
    }
}
