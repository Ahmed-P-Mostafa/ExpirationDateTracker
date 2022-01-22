package com.polotika.expirydatetracker.feature_scan.data.remote

import com.polotika.expirydatetracker.feature_scan.data.local.ProductEntity
import com.polotika.expirydatetracker.feature_scan.domain.model.Product

data class ProductDto(
    val age_group: String,
    val asin: String,
    val barcode_formats: String,
    val barcode_number: String,
    val brand: String,
    val category: String,
    val color: String,
    val contributors: List<ContributorDto>,
    val description: String,
    val energy_efficiency_class: String,
    val features: List<Any>,
    val format: String,
    val gender: String,
    val height: String,
    val images: List<String>,
    val ingredients: String,
    val last_update: String,
    val length: String,
    val manufacturer: String,
    val material: String,
    val model: String,
    val mpn: String,
    val multipack: String,
    val nutrition_facts: String,
    val pattern: String,
    val release_date: String,
    val reviews: List<Any>,
    val size: String,
    val stores: List<StoreDto>,
    val title: String,
    val weight: String,
    val width: String
){
    fun toProductEntity():ProductEntity{
        return ProductEntity(name = title, type = category, expiryDate = 0L)
    }
    fun toProduct():Product{
        return Product(name = title, type = category,1L)
    }
}