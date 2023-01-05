package com.example.wakemeup.auth

import android.content.SharedPreferences
import com.example.wakemeup.retrofit.Retrofit_interface
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthData {

    var gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder().baseUrl("http://3.36.49.22:3000/")
        .addConverterFactory(GsonConverterFactory.create(gson)).build();
    val service = retrofit.create(Retrofit_interface::class.java);

}