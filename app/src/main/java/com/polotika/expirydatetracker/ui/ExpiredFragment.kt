package com.polotika.expirydatetracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.FragmentExpiredBinding
import com.polotika.expirydatetracker.databinding.FragmentHomeBinding
import com.polotika.expirydatetracker.feature_scan.presentation.ExpiredViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpiredFragment : Fragment() {
    private lateinit var binding: FragmentExpiredBinding
    private val viewModel: ExpiredViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpiredBinding.inflate(inflater,container,false)
        binding.vm = viewModel
        viewModel.getProductsFromDatabase()
        return binding.root
    }


}