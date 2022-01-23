package com.polotika.expirydatetracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
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

    private fun showSaveDialog(product: Product) {

        val dialog = MaterialAlertDialogBuilder(this)
        val bindingDialog = SaveDialogLayoutBinding.inflate(LayoutInflater.from(this))
        bindingDialog.vm = viewModel
        bindingDialog.p = viewModel.product.value
        dialog.setView(bindingDialog.root).setTitle("save product").setCancelable(false)
            .setPositiveButton("Scan more") { a, _ ->
                viewModel.addNewProduct {
                    if (!it) {
                        bindingDialog.productDate.error = "Please select one of the options first"
                    } else {
                        a.dismiss()
                    }
                }
                onNewProductClick(binding.scanFab)
            }.setNegativeButton("Save") { a, _ ->
                viewModel.addNewProduct {
                    if (!it) {
                        bindingDialog.productDate.error = "Please select one of the options first"
                    } else {
                        a.dismiss()
                    }
                }
            }.create()
        dialog.show()
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
                        showSaveDialog(it.product)
                    }
                    is HomeDataState.Loading -> {

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