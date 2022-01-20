package com.polotika.expirydatetracker.feature_scan.data.local

import androidx.room.Database

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase {

    abstract val dao:ProductDao
}