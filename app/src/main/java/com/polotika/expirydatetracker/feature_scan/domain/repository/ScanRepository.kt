package com.polotika.expirydatetracker.feature_scan.domain.repository

import com.polotika.expirydatetracker.feature_scan.data.local.ProductEntity
import com.polotika.expirydatetracker.feature_scan.data.remote.BarcodeApi
import com.polotika.expirydatetracker.feature_scan.data.remote.ProductDto
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ScanRepository :BarcodeApi{

    suspend fun addNewProduct(product:Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getNonExpiredProducts(date:Long) : Collection<Product>

    suspend fun getExpiredProducts(date: Long) :Collection<Product>

}