package com.polotika.expirydatetracker.feature_scan.di

import android.content.Context
import androidx.room.Room
import com.polotika.expirydatetracker.BuildConfig
import com.polotika.expirydatetracker.feature_scan.data.local.ProductDatabase
import com.polotika.expirydatetracker.feature_scan.data.remote.BarcodeApi
import com.polotika.expirydatetracker.feature_scan.data.repository.ScanRepositoryImpl
import com.polotika.expirydatetracker.feature_scan.domain.repository.ScanRepository
import com.polotika.expirydatetracker.utils.AppConstats.TIME_OUT_VALUE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanModule {


    @Singleton
    @Provides
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        return loggingInterceptor
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor,loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BarcodeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
    }

    @Singleton
    @Provides
    fun provideBarcodeApi(retrofit: Retrofit.Builder): BarcodeApi {
        return retrofit.build().create(BarcodeApi::class.java)
    }

    @Provides
    fun provideScanRepository(db: ProductDatabase, api: BarcodeApi): ScanRepository {
        return ScanRepositoryImpl(db.dao, api)
    }

    @Provides
    fun provideProductDao(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, "products_db")
            .fallbackToDestructiveMigration().build()
    }


}