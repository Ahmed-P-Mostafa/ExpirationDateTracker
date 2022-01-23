package com.polotika.expirydatetracker.feature_scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polotika.expirydatetracker.feature_scan.di.IoDispatcher
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.use.HomeViewModelUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpiredViewModel @Inject constructor(
    private val useCases: HomeViewModelUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val expiredListAdapter = ProductsRecyclerViewAdapter()

    fun getProductsFromDatabase(): List<Product> = runBlocking {
        viewModelScope.launch(dispatcher) {
            val date = Calendar.getInstance().timeInMillis
            val list = useCases.repository.getExpiredProducts(date).map { it }
            expiredListAdapter.changeDate(list)
        }

        val deferred = async(dispatcher) {
            val date = Calendar.getInstance().timeInMillis
            useCases.repository.getExpiredProducts(date).map { it }
        }
        if (deferred.await().isNotEmpty()) {
            return@runBlocking deferred.await()
        } else {
            return@runBlocking emptyList()
        }

    }
}