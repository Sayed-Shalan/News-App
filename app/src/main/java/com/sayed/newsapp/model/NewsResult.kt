package com.sayed.newsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsResult(var status: String, var totalResults: Int, var articles: List<News>) : Parcelable