package com.esolz.aicafeapp;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.camerapreviewesolz.CameraPreview;

public class EsolzCamera extends AppCompatActivity {

    private CameraPreview mPreview;
    RelativeLayout mLayout;
    ImageView capture, chooseCamera;
    LinearLayout progressBar;
    private boolean cameraFront = false;
    int mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esolz_camera);
        mLayout = (RelativeLayout) findViewById(R.id.surfaceViewbucket);
        capture = (ImageView) findViewById(R.id.captureimage);
        chooseCamera = (ImageView) findViewById(R.id.captureimagefront);
        chooseCamera.setVisibility(View.GONE);
        progressBar = (LinearLayout) findViewById(R.id.progressloader);

        // ------------- Today 28-07-2015 -----------------

        chooseCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cameraFront) {
                        cameraFront = false;
                        mCameraId = 1;
                        Toast.makeText(EsolzCamera.this, "" + mCameraId, Toast.LENGTH_SHORT).show();
                        getCameraPreview();
                    } else {
                        cameraFront = true;
                        mCameraId = 0;
                        Toast.makeText(EsolzCamera.this, "" + mCameraId, Toast.LENGTH_SHORT).show();
                        getCameraPreview();
                    }
                } catch (Exception e) {
                    Log.d("@@  ", e.toString());
                }
            }
        });


        capture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    mPreview.captureImage();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        getCameraPreview();
    }

    public void getCameraPreview() {
        if (cameraFront) {
            LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mPreview = new CameraPreview(EsolzCamera.this, 1, CameraPreview.LayoutMode.FitToParent, progressBar);
            mLayout.addView(mPreview, 0, previewLayoutParams);
        } else {
            LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mPreview = new CameraPreview(EsolzCamera.this, 0, CameraPreview.LayoutMode.FitToParent, progressBar);
            mLayout.addView(mPreview, 0, previewLayoutParams);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }


}
