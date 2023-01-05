package com.example.wakemeup.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class retrofit_client {

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"


    fun getApiService(): Retrofit_interface? {
        return getInstance().create(Retrofit_interface::class.java)
    }

    private fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}