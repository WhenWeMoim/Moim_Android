package com.legends.moim.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass : Application() {
    companion object {
        const val TAG: String = "MOIM-APP"
        const val BASE_URL = "https://unjemoim.shop"

        lateinit var mSharedPreferences: SharedPreferences
        lateinit var retrofit: Retrofit
        //lateinit var retrofitWithoutAccessToken: Retrofit
    }
    override fun onCreate() {
        super.onCreate()

        //KakaoSdk.init(this, "d04b5837217105d4b2a2b16ced721572")

//        val clientWithoutAccessToken: OkHttpClient = OkHttpClient.Builder()
//            .readTimeout(10000, TimeUnit.MILLISECONDS)
//            .connectTimeout(10000, TimeUnit.MILLISECONDS)
//            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

//        retrofitWithoutAccessToken = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(clientWithoutAccessToken)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}