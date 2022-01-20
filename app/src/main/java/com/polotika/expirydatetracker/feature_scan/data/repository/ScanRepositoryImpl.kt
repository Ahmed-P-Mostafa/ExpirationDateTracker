package com.polotika.expirydatetracker.feature_scan.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.polotika.expirydatetracker.feature_scan.data.local.Mapper
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDao
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import com.polotika.expirydatetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanRepositoryImpl(private val dao: ProductDao) : ScanRepository {

    override suspend fun addNewProduct(product: Product) {

        dao.insertProduct(Mapper.toProductEntity(product))
    }

    override suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(Mapper.toProductEntity(product))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getNonExpiredProducts(date: Long): Flow<Resource<Collection<Product>>> =
        flow {
            emit(Resource.Loading())

            val safeProducts = dao.getNonExpiredProducts(date).map {
                Mapper.toProduct(it)
            }
            emit(Resource.Success(safeProducts))
        }

    override suspend fun getExpiredProducts(date: Long): Flow<Resource<Collection<Product>>> =
        flow {
            emit(Resource.Loading())

            val safeProducts = dao.getExpiredProducts(date).map {
                Mapper.toProduct(it)
            }
            emit(Resource.Success(safeProducts))
        }
}