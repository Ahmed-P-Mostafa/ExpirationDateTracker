package com.polotika.expirydatetracker.feature_scan.di

import android.content.Context
import com.polotika.expirydatetracker.R
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val originalUrl = request.  url
        val modifiedUrl = originalUrl.newBuilder()
            .addQueryParameter(CLIENT_ID_KEY, context.getString(R.string.BARCODE_API_KEY))
            .addQueryParameter(QUERY_KEY, RESTAURANT_QUERY).build()
        val modifiedRequest = request.newBuilder().url(modifiedUrl).build()

        return chain.proceed(modifiedRequest)
    }
}