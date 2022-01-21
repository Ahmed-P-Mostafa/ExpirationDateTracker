package com.polotika.expirydatetracker.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.UPC_A
import com.google.zxing.integration.android.IntentIntegrator.UPC_E
import com.google.zxing.integration.android.IntentResult
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanOptions

import com.journeyapps.barcodescanner.ScanContract

import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanIntentResult


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val PERMISSION_CAMERA_REQUEST = 0
    private val CAMERA_INTENT_REQUEST = 1
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.scanFab.setOnClickListener {
            onButtonClick()
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
            Log.d(TAG, "onActivityResult: ${result.barcodeImagePath}")
            Log.d(TAG, "onActivityResult: ${result.contents}")
            Log.d(TAG, "onActivityResult: ${result.formatName}")
            Log.d(TAG, "onActivityResult: ${result.originalIntent.data}")
        }
    }

    // Launch
    fun onButtonClick() {
        val options = ScanOptions()
        options.setOrientationLocked(true)
        options.captureActivity = CapturedActivity::class.java
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        barcodeLauncher.launch(options)
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