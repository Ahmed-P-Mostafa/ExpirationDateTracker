package com.polotika.expirydatetracker.feature_scan.data.local

import com.polotika.expirydatetracker.feature_scan.domain.model.Product

object Mapper {

    fun toProductEntity(product: Product):ProductEntity{
        return ProductEntity(product.name,product.type,product.expiryDate)
    }

    fun toProduct(productEntity: ProductEntity):Product{
        return Product(productEntity.name,productEntity.type,productEntity.expiryDate)
    }
}