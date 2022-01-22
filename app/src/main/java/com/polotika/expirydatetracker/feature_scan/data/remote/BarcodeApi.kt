package com.polotika.expirydatetracker.feature_scan.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.barcodelookup.com/v3/products?barcode=978014015737&formatted=y&key=ifDzhmKslKav42OD93NE
interface BarcodeApi {

    companion object{
        const val BASE_URL = "https://api.barcodelookup.com/v3/"
    }
    @GET("products")
    suspend fun searchForProduct(@Query("barcode") barcode: Long):Response<ProductResponseDto>
}