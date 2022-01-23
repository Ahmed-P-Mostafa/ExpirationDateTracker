package com.polotika.expirydatetracker.feature_track.data.repository

import com.polotika.expirydatetracker.feature_scan.data.local.Mapper
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDao
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_track.domain.repository.TrackRepository
import java.util.*

class TrackRepositoryImpl(private val dao: ProductDao) :TrackRepository  {

    override suspend fun lookForExpiredItems():List<Product> {
        val time = Calendar.getInstance().timeInMillis
        return dao.getExpiredProducts(time).filter { !it.isNotified }.map { Mapper.toProduct(it) }
    }
}