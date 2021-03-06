package com.sayed.newsapp.di

import com.sayed.newsapp.app.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, ActivityModule::class, AppModule::class, ViewModelModule::class, ServiceModule::class))
interface AppComponent {
    //new builder
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App) //To inject app class
}