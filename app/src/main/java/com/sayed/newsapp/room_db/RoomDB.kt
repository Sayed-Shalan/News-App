package com.sayed.newsapp.room_db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(EntityNews::class), version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase(){
    //get DAO
    abstract fun getNewsDAO(): DAONews
}