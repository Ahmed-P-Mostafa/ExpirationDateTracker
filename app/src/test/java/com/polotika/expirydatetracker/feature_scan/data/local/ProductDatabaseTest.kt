package com.polotika.expirydatetracker.feature_scan.data.local

import android.app.AlarmManager.INTERVAL_HOUR
import android.content.Context
import androidx.room.Room
import com.polotika.expirydatetracker.feature_scan.domain.model.Product
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ProductDatabaseTest : TestCase() {
    private lateinit var db: ProductDatabase
    private lateinit var dao: ProductDao
    private val futureTime = Calendar.getInstance().timeInMillis + INTERVAL_HOUR *6
    private val pastTime = Calendar.getInstance().timeInMillis - INTERVAL_HOUR *6

    @Mock
    private lateinit var mockContext: Context

    @Before
    public override fun setUp() {
        db = Room.inMemoryDatabaseBuilder(mockContext, ProductDatabase::class.java).build()
        dao = db.dao
        super.setUp()

    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `getExpiredProducts with valid date then return true`() {
        val expiredProduct = Product("expired", "type", pastTime)
        CoroutineScope(Dispatchers.Unconfined).launch {
            dao.insertProduct(Mapper.toProductEntity(expiredProduct))
            val products = dao.getExpiredProducts(Calendar.getInstance().timeInMillis)
            assertTrue(products.contains(Mapper.toProductEntity(expiredProduct)))
        }

    }

    @Test
    fun `getExpiredProducts with not valid date then return false`() {

        val expiredProduct = Product("expired", "type",futureTime )
        CoroutineScope(Dispatchers.Unconfined).launch {
            dao.insertProduct(Mapper.toProductEntity(expiredProduct))
            val products = dao.getExpiredProducts(Calendar.getInstance().timeInMillis)
            assertFalse(products.contains(Mapper.toProductEntity(expiredProduct)))
        }

    }

    @Test
    fun `getValidProducts with valid date then return true`() {
        val validProduct = Product("valid", "type", futureTime)
        CoroutineScope(Dispatchers.Unconfined).launch {
            dao.insertProduct(Mapper.toProductEntity(validProduct))
            val products = dao.getNonExpiredProducts(Calendar.getInstance().timeInMillis)
            assertTrue(products.contains(Mapper.toProductEntity(validProduct)))
        }

    }

    @Test
    fun `getvalidProducts with not valid date then return false`() {
        val validProduct = Product("valid", "type", pastTime)
        CoroutineScope(Dispatchers.Unconfined).launch {
            dao.insertProduct(Mapper.toProductEntity(validProduct))
            val products = dao.getNonExpiredProducts(Calendar.getInstance().timeInMillis)
            assertTrue(products.contains(Mapper.toProductEntity(validProduct)))
        }

    }
}