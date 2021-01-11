package com.leyline.computervision.opencv

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.leyline.computervision.R
import org.opencv.android.*
import org.opencv.core.CvType
import org.opencv.core.Mat

class OpenCvActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2{


    var mRgba: Mat? = null
    var mRGBAT: Mat? = null
    var javaCameraView: JavaCameraView? = null
    var cycleLeft: Button? = null
    var cycleRight: Button? = null
    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    System.loadLibrary("native-lib")
                    javaCameraView?.enableView()
                    Log.d(TAG, "callback: openCV loaded successfully")
                }
                LoaderCallbackInterface.MARKET_ERROR -> {
                    run { Log.d(TAG, "callback: third option") }
                    run {
                        super.onManagerConnected(status)
                        Log.d(TAG, "callback: could not load openCV")
                    }
                }
                else -> {
                    super.onManagerConnected(status)
                    Log.d(TAG, "callback: could not load openCV")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_cv)
        javaCameraView = findViewById(R.id.java_camera_view)
        cycleLeft = findViewById(R.id.btnCycleFilterLeft)
        cycleRight = findViewById(R.id.btnCycleFilterRight)
        cycleLeft?.setOnClickListener(View.OnClickListener { cycleGrayscale() })
        cycleRight?.setOnClickListener(View.OnClickListener { cycleBGR2RGB() })
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions granted")
            initializeCamera(javaCameraView, activeCamera)
        } else {
            Log.d(TAG, "Permission prompt")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                initializeCamera(javaCameraView, activeCamera)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeCamera(javaCameraView: JavaCameraView?, activeCamera: Int) {
        javaCameraView?.setCameraPermissionGranted()
        javaCameraView?.setCameraIndex(activeCamera)
        javaCameraView?.setVisibility(View.VISIBLE)
        javaCameraView?.setCvCameraViewListener(this)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRGBAT = Mat(height, width, CvType.CV_8UC4)
    }

    override fun onCameraViewStopped() {
        mRgba?.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        val mRgba = inputFrame.rgba()
        if (filterType === "BGR2RGB") {
            NativeClass.bGR2RGB(mRgba?.getNativeObjAddr())
        } else if (filterType === "grayscale") {
            NativeClass.grayScale(mRgba?.getNativeObjAddr())
        }
        return mRgba
    }

    public override fun onResume() {
        Log.d(TAG, "In on Resume")
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            val success: Boolean = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
            if (success) {
                Log.d(TAG, "onResume: Async Init success")
            } else {
                Log.d(TAG, "onResume: Async Init failure")
            }
        } else {
            Log.d(TAG, "onResume: openCV library found inside package.")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (javaCameraView != null) {
            javaCameraView?.disableView()
        }
    }

    override fun onPause() {
        super.onPause()
        if (javaCameraView != null) {
            javaCameraView?.disableView()
        }
    }

    companion object {
        private var filterType = "grayscale"
        private const val TAG = "MIKEY"
        private val activeCamera: Int = CameraBridgeViewBase.CAMERA_ID_BACK
        private const val CAMERA_REQUEST_CODE = 100
        fun cycleGrayscale() {
            filterType = "grayscale"
        }
        fun cycleBGR2RGB() {
            filterType = "BGR2RGB"
        }
    }
}