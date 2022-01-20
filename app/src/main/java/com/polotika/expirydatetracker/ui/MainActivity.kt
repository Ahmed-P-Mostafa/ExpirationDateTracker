package com.polotika.expirydatetracker.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val PERMISSION_CAMERA_REQUEST = 0
    private val CAMERA_INTENT_REQUEST = 1
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.scanFab.setOnClickListener {
            if (isCameraPermissionGranted()){
               startCamera()
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_CAMERA_REQUEST
                )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
               startCamera()
            } else {
                Log.e(TAG, "no camera permission")

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_INTENT_REQUEST&& resultCode == RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(data?.data?.path)

            setupCamera(bitmap)
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PERMISSION_GRANTED
    }

    private fun startCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,CAMERA_INTENT_REQUEST)
    }
    private fun setupCamera(bitmap: Bitmap) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_CODABAR,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val result = scanner.process(InputImage.fromBitmap(bitmap,0))
            .addOnSuccessListener {
                for (barcode: Barcode in it) {

                    when (barcode.valueType) {
                        Barcode.FORMAT_CODABAR -> {
                            Log.d(TAG, "setupCamera: ${barcode.valueType}")
                        }
                    }
                }
        }.addOnFailureListener {

        }

    }
}