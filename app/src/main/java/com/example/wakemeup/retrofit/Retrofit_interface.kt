package com.example.wakemeup.retrofit

import retrofit2.Call
import retrofit2.http.*


interface Retrofit_interface {

    //GET 예제
    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("id") id : String, @Field("psword") psword : String): Call<ResponseData>

    @FormUrlEncoded
    @POST("/register")
    fun join(@Field("id") id : String,@Field("name") name : String,@Field("psword") psword : String ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/alarm")
    fun getAlarms(@Field("id") id : String): Call<DBAlarmDataModel>

    @FormUrlEncoded
    @POST("/alarmRegister")
    fun alarmRegsiter(@Field("alarmNum") alarmNum : Int,@Field("title") title : String,
                      @Field("helper") helper : String,@Field("dorN") dorN : String,
                      @Field("isActivated") isActivated : Int,@Field("isHelperActivated") isHelperActivated : Int,
                      @Field("ringTone") ringTone : String, @Field("id") id : String,
                      @Field("alarmTime") alarmTime : String, @Field("dates") dates : String): Call<ResponseData>


    @DELETE("/alarmDelete")
    fun alarmDelete(@Query("alarmNum") alarmNum : Int, @Query("id") id : String): Call<ResponseData>

    @PUT("/alarmUpdate")
    fun alarmUpdate(@Query("alarmNum") alarmNum : Int,@Query("title") title : String,
                      @Query("helper") helper : String,@Query("dorN") dorN : String,
                      @Query("isActivated") isActivated : Int,@Query("isHelperActivated") isHelperActivated : Int,
                      @Query("ringTone") ringTone : String, @Query("id") id : String,
                      @Query("alarmTime") alarmTime : String, @Query("dates") dates : String): Call<ResponseData>
}