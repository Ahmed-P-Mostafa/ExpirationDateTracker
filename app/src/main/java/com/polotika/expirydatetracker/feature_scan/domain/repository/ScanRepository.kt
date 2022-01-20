package com.polotika.expirydatetracker.feature_scan.domain.repository

import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ScanRepository {

    suspend fun addNewProduct(product:Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getNonExpiredProducts(date:Long) : Flow<Resource<Collection<Product>>>

    suspend fun getExpiredProducts(date: Long) :Flow<Resource<Collection<Product>>>
}