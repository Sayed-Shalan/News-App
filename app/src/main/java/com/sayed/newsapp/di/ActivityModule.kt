package com.sayed.newsapp.di

import com.sayed.newsapp.ui.home.MainActivity
import com.sayed.newsapp.ui.home.NewsDetailFragment
import com.sayed.newsapp.ui.home.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityModule {

    /**
     * Inject activities to be able to request dependencies
     */
    @ContributesAndroidInjector(modules = [NewsModule::class])
    abstract fun contributeMainActivity() : MainActivity


    @Module
    interface NewsModule {

        @ContributesAndroidInjector //For News Fragment
        fun contributeNewsFragment(): NewsFragment

        @ContributesAndroidInjector //For News Detail Fragment
        fun contributeDetailFragment() : NewsDetailFragment
    }

}