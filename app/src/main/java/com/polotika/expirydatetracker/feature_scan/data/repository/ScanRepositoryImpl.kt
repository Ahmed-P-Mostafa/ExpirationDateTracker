package com.polotika.expirydatetracker.feature_scan.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.polotika.expirydatetracker.feature_scan.data.local.Mapper
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDao
import com.polotika.expirydatetracker.feature_scan.data.remote.BarcodeApi
import com.polotika.expirydatetracker.feature_scan.data.remote.ProductResponseDto
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import retrofit2.Response

class ScanRepositoryImpl(private val dao: ProductDao, private val api: BarcodeApi) :
    ScanRepository {

    override suspend fun addNewProduct(product: Product) {
        dao.insertProduct(Mapper.toProductEntity(product))
        Log.d("TAG", "addNewProduct: ")
    }

    override suspend fun deleteProduct(product: Product) =
        dao.deleteProduct(Mapper.toProductEntity(product))


    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getNonExpiredProducts(date: Long): Collection<Product> {
        val list = dao.getNonExpiredProducts(date).map { Mapper.toProduct(it) }
        Log.d("TAG impl", "getNonExpiredProducts: ${list.size}")

        return list
    }



    override suspend fun getExpiredProducts(date: Long): Collection<Product> {
        return dao.getExpiredProducts(date).map { Mapper.toProduct(it) }
    }

    override suspend fun searchForProduct(barcode: Long): Response<ProductResponseDto> =
        api.searchForProduct(barcode)
}