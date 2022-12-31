package com.example.wakemeup

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class AlarmDataModel (
    var dates: ArrayList<Boolean>, var title : String, var helper : String,
    var dorN : String, var isActivated : Boolean,
    var isHelperActivated : Boolean,
    var ringTone : Uri, var time : String) : Serializable, Parcelable {

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}
