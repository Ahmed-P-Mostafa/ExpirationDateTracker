package com.polotika.expirydatetracker.feature_scan.domain.usecases

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDao
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDatabase
import com.polotika.expirydatetracker.feature_scan.data.remote.BarcodeApi
import com.polotika.expirydatetracker.feature_scan.data.remote.ProductResponseDto
import com.polotika.expirydatetracker.feature_scan.data.repository.ScanRepositoryImpl
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import com.polotika.expirydatetracker.feature_scan.domain.use.GetProductUseCase
import com.polotika.expirydatetracker.feature_scan.domain.use.HomeViewModelUseCases
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelUseCasesTest :TestCase(){

    @Test
    fun `searchForProduct() with not valid barcode return random product`(){
        // arrange
        val barcode = 67792815455L
        val randomNames = arrayOf("Milk","Egg","Orange juice")

        // act
        CoroutineScope(Dispatchers.Unconfined).launch {
            val result = useCaseTest.invoke(barcode)

            // assert
            assertTrue(randomNames.contains(result.data?.name))
        }

    }

    @Before
    public override fun setUp() {
        db = Room.inMemoryDatabaseBuilder(mockContext, ProductDatabase::class.java).build()
        dao = db.dao

        repository = ScanRepositoryImpl(db.dao, object : BarcodeApi {
            override suspend fun searchForProduct(barcode: Long): Response<ProductResponseDto> {
                return Response.success(ProductResponseDto())
            }
        })
        useCaseTest = GetProductUseCase(repository)
        homeViewModelUseCases = HomeViewModelUseCases(useCaseTest,repository)
        super.setUp()

    }
    @After
    fun onStop(){
        db.close()
    }

    @Mock
    private lateinit var mockContext: Context
    private lateinit var homeViewModelUseCases: HomeViewModelUseCases

    private lateinit var dao :ProductDao
    private lateinit var db :ProductDatabase

    private lateinit var repository: ScanRepository
    private lateinit var useCaseTest: GetProductUseCase




}