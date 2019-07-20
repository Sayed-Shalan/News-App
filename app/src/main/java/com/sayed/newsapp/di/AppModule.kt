package com.sayed.newsapp.di

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sayed.newsapp.app.App
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
    fun provideSPUtils(app : Context): SPUtils {
        return SPUtils(app)
    }



}