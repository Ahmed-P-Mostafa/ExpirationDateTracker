package com.polotika.expirydatetracker.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.polotika.expirydatetracker.databinding.FragmentHomeBinding
import com.polotika.expirydatetracker.databinding.SaveDialogLayoutBinding
import com.polotika.expirydatetracker.feature_scan.presentation.HomeDataState
import com.polotika.expirydatetracker.feature_scan.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        viewModel.activityIntentResult(result)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        viewModel.getProductsFromDatabase()
        observers()
        binding.scanFab.setOnClickListener {
            viewModel.onNewProduct { scanOptions ->
                barcodeLauncher.launch(scanOptions)
            }
        }
        return binding.root
    }

    private fun showSaveDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val bindingDialog = SaveDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        bindingDialog.vm = viewModel
        bindingDialog.p = viewModel.product.value
        builder
            .setView(bindingDialog.root)
            .setTitle("save product")
            .setCancelable(false)
            .setPositiveButton("Scan more") { _, _ -> }
            .setNegativeButton("Save") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            viewModel.addNewProduct {
                if (!it) {
                    bindingDialog.productDate.error = "Please select one of the options first"
                } else {
                    dialog.dismiss()
                    viewModel.onNewProduct {
                        barcodeLauncher.launch(it)
                    }
                }
            }
        }
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
            viewModel.addNewProduct {
                if (!it) {
                    bindingDialog.productDate.error = "Please select one of the options first"
                } else {
                    dialog.dismiss()
                }
            }

        }
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


}