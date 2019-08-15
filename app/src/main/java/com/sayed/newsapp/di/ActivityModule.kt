package com.sayed.newsapp.di

import com.sayed.newsapp.ui.home.*
import com.sayed.newsapp.ui.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

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

        @ContributesAndroidInjector //For Login Fragment
        fun contributeLoginFragment() : LoginFragment

        @ContributesAndroidInjector //For Home Fragment
        fun contributeHomeFragment() : HomeFragment

        @ContributesAndroidInjector //For Local news Fragment
        fun contributeLocalFragment() : LocalFragment

        @ContributesAndroidInjector //For Add news Fragment
        fun contributeAddNewsFragment() : AddNewsFragment
    }

}