package com.polotika.expirydatetracker.feature_scan.presentation

import androidx.recyclerview.widget.DiffUtil
import com.polotika.expirydatetracker.feature_scan.domain.model.Product

class ProductsDiffUtil(private val oldList: List<Product>, private val newList: List<Product>):DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}