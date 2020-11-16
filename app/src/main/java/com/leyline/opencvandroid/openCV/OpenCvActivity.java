package com.leyline.opencvandroid.openCV;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.leyline.opencvandroid.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class OpenCvActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

        private static final String TAG = "MIKEY";
        private static int activeCamera = CameraBridgeViewBase.CAMERA_ID_BACK;
        private static final int requestCode = 100;
        JavaCameraView javaCameraView;

        private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        javaCameraView.enableView();
                        Log.d(TAG, "callback: openCV loaded successfully");
                        break;
                    }
                    case LoaderCallbackInterface.MARKET_ERROR: {
                        Log.d(TAG, "callback: third option");
                    }
                    default: {
                        super.onManagerConnected(status);
                        Log.d(TAG, "callback: could not load openCV");
                    }
                }
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_open_cv);
            javaCameraView = findViewById(R.id.java_camera_view);
            // checking if the permission has already been granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissions granted");
                initializeCamera(javaCameraView, activeCamera);
            } else {
                // prompt system dialog
                Log.d(TAG, "Permission prompt");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCode);
            }
        }

        private void initializeCamera(JavaCameraView javaCameraView, int activeCamera){
            javaCameraView.setCameraPermissionGranted();
            javaCameraView.setCameraIndex(activeCamera);
            javaCameraView.setVisibility(View.VISIBLE);
            javaCameraView.setCvCameraViewListener(this);
        }

        @Override
        public void onCameraViewStarted(int width, int height) {

        }

        @Override
        public void onCameraViewStopped() {

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            return inputFrame.rgba();
        }

        public void onResume(){
            Log.d(TAG, "In on Resume");
            super.onResume();
            if(!OpenCVLoader.initDebug()){
                boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
                if(success){
                    Log.d(TAG,"onResume: Async Init success");
                }else{
                    Log.d(TAG,"onResume: Async Init failure");
                }
            }else{
                Log.d(TAG, "onResume: openCV library found inside package.");
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // camera can be turned on
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                initializeCamera(javaCameraView, activeCamera);
            } else {
                // camera will stay off
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    }
