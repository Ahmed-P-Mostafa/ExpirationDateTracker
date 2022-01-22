package com.polotika.expirydatetracker.feature_scan.data.remote

data class TaxDto(
    val country: String,
    val rate: String,
    val region: String,
    val tax_ship: String
)