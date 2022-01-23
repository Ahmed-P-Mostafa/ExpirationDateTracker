package com.polotika.expirydatetracker.feature_scan.domain.use

import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository


data class HomeViewModelUseCases(val getProduct: GetProductUseCase ,val repository: ScanRepository
)