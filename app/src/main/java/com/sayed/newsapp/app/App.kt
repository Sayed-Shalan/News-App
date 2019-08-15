package com.sayed.newsapp.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.sayed.newsapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {


    //Dec Data
    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>


    /**
     * Start On App Create *************************
     */
    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().application(this).build().inject(this) //inject this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    //To inject activities with traditional way
    override fun activityInjector(): AndroidInjector<Activity> {
        return this.dispatchingAndroidActivityInjector
    }
}