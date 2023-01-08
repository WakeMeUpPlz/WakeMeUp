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
    fun alarmRegsiter(@Field("alarmNum") alarmNum : Int, @Field("title") title : String,
                      @Field("helper") helper : String,@Field("dorN") dorN : String,
                      @Field("isActivated") isActivated : Int,@Field("isHelperActivated") isHelperActivated : Int,
                      @Field("ringTone") ringTone : String, @Field("id") id : String,
                      @Field("alarmTime") alarmTime : String, @Field("dates") dates : String): Call<ResponseData>

    @FormUrlEncoded
    @POST("/alarmDelete")
    fun alarmDelete(@Field("alarmNum") alarmNum : Int, @Field("id") id : String): Call<ResponseData>

    @FormUrlEncoded
    @POST("/alarmUpdate")
    fun alarmUpdate(@Field("alarmNum") alarmNum : Int,@Field("title") title : String,
                      @Field("helper") helper : String,@Field("dorN") dorN : String,
                      @Field("isActivated") isActivated : Int,@Field("isHelperActivated") isHelperActivated : Int,
                      @Field("ringTone") ringTone : String, @Field("id") id : String,
                      @Field("alarmTime") alarmTime : String, @Field("dates") dates : String): Call<ResponseData>
}