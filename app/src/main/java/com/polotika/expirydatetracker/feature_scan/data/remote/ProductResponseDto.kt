package com.polotika.expirydatetracker.feature_scan.data.remote

import com.google.gson.annotations.SerializedName


data class ProductResponseDto(

    @SerializedName("products") var products: ArrayList<ProductDto> = arrayListOf()

)
