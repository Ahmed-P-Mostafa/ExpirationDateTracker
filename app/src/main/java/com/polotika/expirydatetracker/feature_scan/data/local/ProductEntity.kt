package com.polotika.expirydatetracker.feature_scan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    val name: String,
    val type: String,
    val expiryDate: Long,
    val isNotified :Boolean = false,
    @PrimaryKey val id: Int? = null
)