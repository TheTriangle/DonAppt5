package com.example.donappt5.data.services

import android.content.Context
import com.google.android.datatransport.runtime.dagger.Module
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class BackendService {
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(
            60,
            TimeUnit.SECONDS
        )
        .readTimeout(
            60,
            TimeUnit.SECONDS
        )
        .build()

    fun provideApiService(
        context: Context,
        json: Json,
        okHttpClient: OkHttpClient
    ): BackendService = Retrofit.Builder()
        .baseUrl("Your api base url")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)

    companion object {
        val BACKEND_ENDPOINT = "https://044f-2a02-a474-ac-1-e4c3-f227-4ab6-b8c7.ngrok-free.app"
    }
}