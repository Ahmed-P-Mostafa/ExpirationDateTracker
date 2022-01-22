package com.polotika.expirydatetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ActivityHomeBinding
import com.polotika.expirydatetracker.databinding.SaveDialogLayoutBinding
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.presentation.HomeDataState
import com.polotika.expirydatetracker.feature_scan.presentation.HomeViewModel
import com.polotika.expirydatetracker.feature_scan.presentation.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.vm = viewModel
        binding.scanFab.setOnClickListener {
            onButtonClick()
        }
        observers()

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
/*
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when(it){
                    is HomeDataState.Loading->{

                    }
                    is HomeDataState.Success->{

                    }
                    is HomeDataState.Failed->{

                    }
                }
            }

            viewModel.eventFlow.collectLatest {
                when(it){
                    is UIEvent.ShowSnackBar ->{
                        Log.d(TAG, "onResume: snackbar")
                        Snackbar.make(binding.root,it.message,Snackbar.LENGTH_SHORT).show()
                    }
                    is UIEvent.ShowSaveDialog ->{
                        Log.d(TAG, "onResume: dialog")

                        showSaveDialog(it.product)
                    }
                }
            }
        }
*/
        super.onResume()
    }

    private fun showSaveDialog(product: Product) {

        val dialog = MaterialAlertDialogBuilder(this)
        val bindingDialog = SaveDialogLayoutBinding.inflate(LayoutInflater.from(this))
        bindingDialog.vm = viewModel
        bindingDialog.p = viewModel.iProduct
      /*  val dialog = MaterialAlertDialogBuilder(this)
        val customView = layoutInflater.inflate(R.layout.save_dialog_layout, null)
        val dateTextView: AutoCompleteTextView = customView.findViewById(R.id.product_date)
        dateTextView.setAdapter(arrayAdapter)
        val name = customView.findViewById<TextView>(R.id.product_name)
        name.setText(product.name)
        val type = customView.findViewById<TextView>(R.id.product_type)
        type.setText(product.type)*/
        dialog.setView(bindingDialog.root).setTitle("save product")
            .setPositiveButton("Scan again") { _, _ ->

            }.setNegativeButton("Save") { _, _ ->

            }.show()

    }


    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        viewModel.activityIntentResult(result)
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

    private fun observers() {


        this.lifecycleScope.launch {
            viewModel.shareFlow.collectLatest {
                when (it) {
                    is UIEvent.ShowSnackBar -> {
                        Log.d(TAG, "onResume: snackbar")
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is UIEvent.ShowSaveDialog -> {
                        Log.d(TAG, "onResume: dialog")

                        showSaveDialog(it.product)
                    }
                }
            }

        }

    }
}