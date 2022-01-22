package com.polotika.expirydatetracker.feature_scan.presentation

import android.R
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanIntentResult
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ScanRepository) : ViewModel() {
    private val TAG = "HomeViewModel"

    val iProduct = Product("name","type",554L)

    private val _shareFlow = MutableSharedFlow<UIEvent>()
    val shareFlow = _shareFlow.asSharedFlow()

    val homeListAdapter: HomeRecyclerViewAdapter
        get() {

            var list = emptyList<Product>()
            viewModelScope.launch {
                val date = Calendar.getInstance().timeInMillis
                list = repository.getNonExpiredProducts(date).map { it }
            }
            return HomeRecyclerViewAdapter(list)
        }

    fun activityIntentResult(result: ScanIntentResult) {
        viewModelScope.launch {
            if (result.contents != null) {
                _shareFlow.emit(UIEvent.ShowSaveDialog(Product(name = "name", type = "type", expiryDate = 121L)))
                try {
                    /*repository.searchForProduct(result.contents.toLong()).apply {
                        if (isSuccessful && code() in 199..299) {
                            if (body()?.products?.isNotEmpty()!!) {
                                _productFlow.emit(body()?.products?.get(0)?.toProduct()!!)
                                _eventFlow.emit(UIEvent.ShowSaveDialog(body()?.products?.get(0)?.toProduct()!!))
                                state.postValue("dialog")
                            } else showErrorSnackBar("Product not found")
                        } else showErrorSnackBar("Product not found")
                    }*/
                } catch (e: HttpException) { showErrorSnackBar("Product not found") }
            } else {
                showErrorSnackBar("Not scanned")
            }
            //state.postValue("snackbar")
        }
    }

    private suspend fun showErrorSnackBar(message:String){
        _shareFlow.emit(UIEvent.ShowSnackBar(message))
    }
}

sealed class HomeDataState() {
    class Success() : HomeDataState()
    class Failed() : HomeDataState()
    object Loading : HomeDataState()
}

sealed class UIEvent() {
    data class ShowSnackBar(val message: String) : UIEvent()
    data class ShowSaveDialog(val product:Product):UIEvent()
}