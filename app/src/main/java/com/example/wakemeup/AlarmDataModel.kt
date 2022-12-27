package com.example.wakemeup

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class AlarmDataModel(
    var dates: ArrayList<Boolean>, var title : String, var helper : String,
    var dorN : String, var isActivated : Boolean,
    var isHelperActivated : Boolean,
    var ringTone : String, var time : String) : Serializable
