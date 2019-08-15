package com.sayed.newsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var id: Int=0, var first_name: String="", var last_name: String="", var email: String="") : Parcelable