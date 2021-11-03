package com.example.appdid.RetrofitSet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {
    companion object
    {
        //build를 간단히 하기 위한 함수
        public fun defaultRetrofit(url:String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }



}