package com.polotika.expirydatetracker.feature_track.domain.repository

import com.polotika.expirydatetracker.feature_scan.domain.model.Product

interface TrackRepository {
    suspend fun lookForExpiredItems():List<Product>
}