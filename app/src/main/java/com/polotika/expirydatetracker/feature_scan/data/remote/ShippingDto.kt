package com.polotika.expirydatetracker.feature_scan.data.remote

data class ShippingDto(
    val country: String,
    val price: String,
    val region: String,
    val service: String
)