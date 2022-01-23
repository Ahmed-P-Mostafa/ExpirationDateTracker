package com.polotika.expirydatetracker.feature_scan.domain.repository

import com.polotika.expirydatetracker.feature_scan.data.remote.BarcodeApi
import com.polotika.expirydatetracker.feature_scan.domain.model.Product

interface ScanRepository :BarcodeApi{

    suspend fun addNewProduct(product:Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getNonExpiredProducts(date:Long) : Collection<Product>

    suspend fun getExpiredProducts(date: Long) :Collection<Product>

}