package com.polotika.expirydatetracker.feature_scan.data.remote

import com.google.mlkit.vision.barcode.common.Barcode
import retrofit2.http.GET
import retrofit2.http.Path

// https://api.barcodelookup.com/v3/products?barcode=978014015737&formatted=y&key=ifDzhmKslKav42OD93NE
interface ProductsApi {

    companion object{
        const val BASE_URL = "https://api.barcodelookup.com/v3"
    }
    @GET("/products?barcode={barcode}&formatted=y")
    fun searchForProduct(@Path("barcode") barcode: String)
}