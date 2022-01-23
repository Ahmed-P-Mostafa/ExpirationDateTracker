package com.polotika.expirydatetracker.ui

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ActivityHomeBinding
import com.polotika.expirydatetracker.databinding.SaveDialogLayoutBinding
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.presentation.HomeDataState
import com.polotika.expirydatetracker.feature_scan.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.vm = viewModel
        viewModel.getProductsFromDatabase()
        observers()

    }

    private fun showSaveDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val bindingDialog = SaveDialogLayoutBinding.inflate(LayoutInflater.from(this))
        bindingDialog.vm = viewModel
        bindingDialog.p = viewModel.product.value
        builder
            .setView(bindingDialog.root)
            .setTitle("save product")
            .setCancelable(false)
            .setPositiveButton("Scan more"){_,_ ->}
            .setNegativeButton("Save"){_,_ ->}
        val dialog :AlertDialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            viewModel.addNewProduct {
                if (!it) {
                    bindingDialog.productDate.error = "Please select one of the options first"
                } else {
                    dialog.dismiss()
                    onNewProductClick(binding.scanFab)
                }
            }
        }
        dialog.getButton(BUTTON_NEGATIVE).setOnClickListener {
            viewModel.addNewProduct {
                if (!it) {
                    bindingDialog.productDate.error = "Please select one of the options first"
                } else {
                    dialog.dismiss()
                }
            }

        }
    }


    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        viewModel.activityIntentResult(result)
    }

    private fun observers() {
        this.lifecycleScope.launch {
            viewModel.uiFlow.collectLatest {
                when (it) {
                    is HomeDataState.Failed -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is HomeDataState.Success -> {
                        showSaveDialog()
                    }

                }
            }
        }
    }

    fun onNewProductClick(view: android.view.View) {
        viewModel.onNewProduct { scanOptions ->
            barcodeLauncher.launch(scanOptions)
        }
    }
}