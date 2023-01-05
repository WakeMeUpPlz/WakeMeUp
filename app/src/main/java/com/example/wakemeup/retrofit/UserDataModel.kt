package com.example.wakemeup.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("success")
    @Expose
    val success: String,
    @SerializedName("msg")
    @Expose
    val msg: String
)


