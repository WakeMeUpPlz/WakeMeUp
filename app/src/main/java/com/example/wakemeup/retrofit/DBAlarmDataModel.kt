package com.example.wakemeup.retrofit

import com.example.wakemeup.AlarmDataModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DBAlarmDataModel(
    @SerializedName("success")
    @Expose
    val success: String,

    @SerializedName("result")
    @Expose
    val result: List<AlarmData>,
)

data class AlarmData(
    @SerializedName("alarmNum")
    @Expose
    val alarm_id: Int,

    @SerializedName("alarmTime")
    @Expose
    val time: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("sound")
    @Expose
    val ringTone: String,

    @SerializedName("helper")
    @Expose
    val helper: String,

    @SerializedName("dates")
    @Expose
    val dates: String,

    @SerializedName("isActivated")
    @Expose
    val isActivated: Int,

    @SerializedName("isHelperActivated")
    @Expose
    val isHelperActivated: Int,

    @SerializedName("dorN")
    @Expose
    val dorN: String,

) : Serializable
