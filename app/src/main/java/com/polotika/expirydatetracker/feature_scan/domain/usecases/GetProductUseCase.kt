package com.polotika.expirydatetracker.feature_scan.domain.use

import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import com.polotika.expirydatetracker.utils.Resource
import retrofit2.HttpException

class GetProductUseCase(private val repository: ScanRepository) {

    suspend fun invoke(barcode:Long):Resource<Product>{

        //return getMockedProduct()
         try {
            repository.searchForProduct(barcode).apply {
                return if (isSuccessful && code() in 199..299) {
                    if (body()?.products?.isNotEmpty()!!) {
                        Resource.Success(this.body()?.products?.get(0)?.toProduct())

                    } else {
                        getMockedProduct()
                    }
                } else {
                    getMockedProduct()
                }
            }
        } catch (e: HttpException) {
            return Resource.Error("Product not found",null)
        }

    }

    private fun getMockedProduct():Resource<Product> {
        val randomNames = arrayOf("Milk","Egg","Orange juice")
        return Resource.Success(Product(name = randomNames.random(), type = "Food",1L))
    }

}