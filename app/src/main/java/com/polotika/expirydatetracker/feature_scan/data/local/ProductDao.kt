package com.polotika.expirydatetracker.feature_scan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("select * from productentity where expiryDate < :date")
    suspend fun getExpiredProducts(date:Long):List<ProductEntity>


    @Query("select * from productentity where expiryDate < :date")
    suspend fun getNonExpiredProducts(date: Long)
}