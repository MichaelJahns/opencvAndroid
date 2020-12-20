package com.leyline.opencvandroid.openCV;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.leyline.opencvandroid.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCvActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    Mat mRgba, mRGBAT;

    private static String filterType = "grayscale";
    private static final String TAG = "MIKEY";
    private static int activeCamera = CameraBridgeViewBase.CAMERA_ID_BACK;
    private static final int CAMERA_REQUEST_CODE = 100;
    JavaCameraView javaCameraView;
    Button cycleLeft, cycleRight;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    System.loadLibrary("native-lib");
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
        cycleLeft = findViewById(R.id.btnCycleFilterLeft);
        cycleRight = findViewById(R.id.btnCycleFilterRight);

        cycleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleGrayscale();
            }
        });
        cycleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleBGR2RGB();
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions granted");
            initializeCamera(javaCameraView, activeCamera);
        } else {
            Log.d(TAG, "Permission prompt");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                initializeCamera(javaCameraView, activeCamera);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeCamera(JavaCameraView javaCameraView, int activeCamera) {
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCameraIndex(activeCamera);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBAT = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if (filterType == "BGR2RGB") {
            NativeClass.BGR2RGB(mRgba.getNativeObjAddr());
        } else if (filterType == "grayscale") {
            NativeClass.grayScale(mRgba.getNativeObjAddr());
        }
        return mRgba;
    }

    public void onResume() {
        Log.d(TAG, "In on Resume");
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
            if (success) {
                Log.d(TAG, "onResume: Async Init success");
            } else {
                Log.d(TAG, "onResume: Async Init failure");
            }
        } else {
            Log.d(TAG, "onResume: openCV library found inside package.");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    public static void cycleGrayscale() {
        filterType = "grayscale";
    }

    public static void cycleBGR2RGB() {
        filterType = "BGR2RGB";
    }
}
