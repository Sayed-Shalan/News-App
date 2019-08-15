package com.sayed.newsapp.di

import android.arch.persistence.room.Room
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sayed.newsapp.app.App
import com.sayed.newsapp.room_db.DAONews
import com.sayed.newsapp.room_db.RoomDB
import com.sayed.newsapp.utils.SPUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    //provide singleton context
    @Singleton
    @Provides
    fun provideContext(app : App) : Context{
        return app;
    }

    //provide SPUtils object
    @Provides
    @Singleton
    fun provideSPUtils(app : Context): SPUtils {
        return SPUtils(app)
    }

    //provide RoomDB Object - singleton
    @Provides
    @Singleton
    fun provideRoomDB(app: App): RoomDB{
        return Room.databaseBuilder(app, RoomDB::class.java, "mydb")
            .allowMainThreadQueries()
            .build()
    }

    //provide NEWS DAO - singleton
    @Provides
    @Singleton
    fun provideNewsDAO(roomDB: RoomDB) : DAONews{
        return roomDB.getNewsDAO()
    }




}