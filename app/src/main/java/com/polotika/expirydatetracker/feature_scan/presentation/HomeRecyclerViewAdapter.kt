package com.polotika.expirydatetracker.feature_scan.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ProductItemBinding
import com.polotika.expirydatetracker.feature_scan.domain.model.Product

class HomeRecyclerViewAdapter(private val list:List<Product>?=null) :RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ProductItemBinding.inflate(inflater,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list?.get(position)
        holder.bind(item!!)
    }

    override fun getItemCount() = list?.size?: 0

    class ViewHolder(private val binding:ProductItemBinding) :RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product){
            binding.apply {
                p = product
                executePendingBindings()
            }
        }

    }


}