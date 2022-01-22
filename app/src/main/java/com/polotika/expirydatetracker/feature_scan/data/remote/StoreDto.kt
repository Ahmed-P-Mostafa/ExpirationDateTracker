package com.polotika.expirydatetracker.feature_scan.data.remote

data class StoreDto(
    val availability: String,
    val condition: String,
    val country: String,
    val currency: String,
    val currency_symbol: String,
    val item_group_id: String,
    val last_update: String,
    val link: String,
    val name: String,
    val price: String,
    val sale_price: String,
    val shipping: List<ShippingDto>,
    val tax: List<TaxDto>
)