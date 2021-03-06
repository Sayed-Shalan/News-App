package com.sayed.newsapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sayed.newsapp.ui.login.LoginViewModel
import com.sayed.newsapp.view_model.LocalNewsViewModel
import com.sayed.newsapp.view_model.NewsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    //Provide Factory
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * Provide View models
     */
    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(newsViewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(newsViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalNewsViewModel::class)
    abstract fun bindLocalNewsViewModel(localNewsViewModel: LocalNewsViewModel): ViewModel


}