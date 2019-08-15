package com.sayed.newsapp.view_model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sayed.newsapp.api.AppResource
import com.sayed.newsapp.model.NewsResult
import com.sayed.newsapp.repositories.NewsRepository
import com.sayed.newsapp.ui.base.BaseViewModel
import javax.inject.Inject

class NewsViewModel @Inject constructor(newsRepository: NewsRepository) : BaseViewModel() {

    //Init Life data
    private val pullNewsLD=MutableLiveData<String>()
    private val resultNewsLD: LiveData<AppResource<NewsResult>>

    //Init life data
    init {
        resultNewsLD=Transformations.switchMap(pullNewsLD,newsRepository::getNews)
    }

    //Observe
    fun observeNews(): LiveData<AppResource<NewsResult>>{
        return resultNewsLD
    }

    //Pull
    fun pullNews(api_key: String){
        pullNewsLD.postValue(api_key)
    }

}