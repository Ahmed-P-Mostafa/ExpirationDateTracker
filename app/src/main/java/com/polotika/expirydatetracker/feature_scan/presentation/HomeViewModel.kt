package com.polotika.expirydatetracker.feature_scan.presentation

import android.app.AlarmManager.INTERVAL_HOUR
import android.widget.AdapterView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.polotika.expirydatetracker.feature_scan.di.IoDispatcher
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.use.HomeViewModelUseCases
import com.polotika.expirydatetracker.ui.CapturedActivity
import com.polotika.expirydatetracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeViewModelUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uieFlow = MutableSharedFlow<HomeDataState>()
    val uiFlow = _uieFlow.asSharedFlow()

    var product = MutableLiveData<Product>(Product("name", "type", 554L))
    val loadingProgress = ObservableBoolean(false)
    val datesListener = AdapterView.OnItemClickListener { _, _, position, _ ->

        val expiryHours = (position + 1) * 6L * INTERVAL_HOUR
        val time = Calendar.getInstance().timeInMillis
        product.value = product.value?.copy(expiryDate = expiryHours + time)
    }

    val homeListAdapter = ProductsRecyclerViewAdapter()
    fun getProductsFromDatabase(){
        viewModelScope.launch{
            val date = Calendar.getInstance().timeInMillis
            val list = useCases.repository.getNonExpiredProducts(date).map { it }
            homeListAdapter.changeDate(list)
        }
    }

    fun onNewProduct(barCodeLauncherOptions: (ScanOptions) -> Unit) {
        val options = ScanOptions()
        options.setOrientationLocked(false)
        options.captureActivity = CapturedActivity::class.java
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        barCodeLauncherOptions(options)
    }

    fun activityIntentResult(result: ScanIntentResult) {
        viewModelScope.launch(dispatcher) {
            loadingProgress.set(true)
            if (result.contents != null) {
                val productResponse = useCases.getProduct.invoke(result.contents.toLong())
                when (productResponse) {
                    is Resource.Success -> {
                        product.postValue( productResponse.data)
                        _uieFlow.emit(HomeDataState.Success(productResponse.data!!))
                    }
                    is Resource.Error -> {
                        _uieFlow.emit(HomeDataState.Failed(productResponse.message!!))
                    }
                    is Resource.Loading -> {
                        _uieFlow.emit(HomeDataState.Loading)
                        loadingProgress.set(true)
                    }
                }
            } else {
                _uieFlow.emit(HomeDataState.Failed("Not Scanned"))
            }
            loadingProgress.set(false)
        }
    }

    fun addNewProduct(isDataValid: (Boolean) -> Unit) {
        if (product.value?.expiryDate!! > Calendar.getInstance().timeInMillis) {
            isDataValid(true)
            viewModelScope.launch(dispatcher) {
                useCases.repository.addNewProduct(product.value!!)
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