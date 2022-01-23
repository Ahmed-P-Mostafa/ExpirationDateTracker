package com.polotika.expirydatetracker.feature_scan.presentation

import android.app.AlarmManager.INTERVAL_HOUR
import android.util.Log
import android.widget.AdapterView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import com.polotika.expirydatetracker.feature_scan.domain.use.GetProductUseCase
import com.polotika.expirydatetracker.ui.CapturedActivity
import com.polotika.expirydatetracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ScanRepository,
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {
    private val TAG = "HomeViewModel"

    private val _uieFlow = MutableSharedFlow<HomeDataState>()
    val uiFlow = _uieFlow.asSharedFlow()

    var product = MutableLiveData<Product>(Product("name", "type", 554L))
    val loadingProgress = ObservableBoolean(false)
    val datesListener = AdapterView.OnItemClickListener { parent, view, position, id ->

        val expiryHours = (position + 1) * 6L * INTERVAL_HOUR
        Log.d(TAG, ": $position")
        Log.d(TAG, ": ${expiryHours / 1000 / 60 / 60}")
        val time = Calendar.getInstance().timeInMillis
        //1642888916554
        product.value = product.value?.copy(expiryDate = expiryHours + time)
    }

    val homeListAdapter = HomeRecyclerViewAdapter()
    fun getProductsFromDatabase() :List<Product> = runBlocking{
        viewModelScope.launch {
            val date = Calendar.getInstance().timeInMillis
            val list = repository.getNonExpiredProducts(date).map { it }
            Log.d(TAG, "getProductsFromDatabase: ${list.size}")

            homeListAdapter.changeDate(list)
        }

            val deferred = async {
                val date = Calendar.getInstance().timeInMillis
                repository.getNonExpiredProducts(date).map { it }
            }
            if (deferred.await().isNotEmpty()){
                return@runBlocking deferred.await()
            }else{
                return@runBlocking emptyList()
            }

    }

    fun onNewProduct(barCodeLauncherOptions: (ScanOptions) -> Unit) {
        val options = ScanOptions()
        options.setOrientationLocked(true)
        options.captureActivity = CapturedActivity::class.java
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        barCodeLauncherOptions(options)
    }

    fun activityIntentResult(result: ScanIntentResult) {
        viewModelScope.launch {
            loadingProgress.set(true)
            if (result.contents != null) {
                val productResponse = getProductUseCase.invoke(result.contents.toLong())
                when (productResponse) {
                    is Resource.Success -> {
                        product.value = productResponse.data
                        _uieFlow.emit(HomeDataState.Success(productResponse.data!!))
                        loadingProgress.set(false)
                    }
                    is Resource.Error -> {
                        _uieFlow.emit(HomeDataState.Failed(productResponse.message!!))
                        loadingProgress.set(false)
                    }
                    is Resource.Loading -> {
                        _uieFlow.emit(HomeDataState.Loading)
                        loadingProgress.set(true)
                    }
                }
            } else {
                _uieFlow.emit(HomeDataState.Failed("Not Scanned"))
            }
        }
    }

    fun addNewProduct(isDataValid: (Boolean) -> Unit) {
        if (product.value?.expiryDate!! > Calendar.getInstance().timeInMillis) {
            isDataValid(true)
            viewModelScope.launch {
                repository.addNewProduct(product.value!!)
                getProductsFromDatabase()
            }
        } else {
            isDataValid(false)
        }
    }
}

sealed class HomeDataState() {
    class Success(val product: Product) : HomeDataState()
    class Failed(val message: String) : HomeDataState()
    object Loading : HomeDataState()
}