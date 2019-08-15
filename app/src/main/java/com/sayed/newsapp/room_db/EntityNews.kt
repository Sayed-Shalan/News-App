package com.sayed.newsapp.room_db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "news") //CREATE ENTITY USING ROOM ANNOTATIONS
data class EntityNews(@PrimaryKey(autoGenerate = true) var id:Long?,
                      @ColumnInfo(name = "title") var title: String,
                      @ColumnInfo(name = "description") var description: String,
                      var swipe: Boolean=false) : Parcelable {
    constructor() : this(null,"","")
}