package com.polotika.expirydatetracker.feature_scan.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.polotika.expirydatetracker.databinding.ProductItemBinding
import com.polotika.expirydatetracker.feature_scan.domain.model.Product

class HomeRecyclerViewAdapter(var list:List<Product>?= emptyList()) :RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Log.d("TAG", "onCreateViewHolder: ${list?.size}")
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
            binding.p = product
            binding.invalidateAll()
        }

    }

    fun changeDate(newList:List<Product>){
        Log.d("TAG", "changeDate: ${newList.size}")
        val diffUtil = ProductsDiffUtil(newList,list?: emptyList())
        val results = DiffUtil.calculateDiff(diffUtil)
        list = newList
        results.dispatchUpdatesTo(this)
    }


}