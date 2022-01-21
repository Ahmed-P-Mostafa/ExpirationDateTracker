package com.polotika.expirydatetracker.feature_scan.di

import com.polotika.expirydatetracker.feature_scan.data.remote.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ScanModule {

    @Provides
    fun provideRetrofit(): ProductsApi {
        return Retrofit.Builder()
            .baseUrl(ProductsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client()
            .build()
            .create(ProductsApi::class.java)
    }
}