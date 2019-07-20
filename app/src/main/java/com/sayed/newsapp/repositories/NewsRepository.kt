package com.sayed.newsapp.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sayed.newsapp.api.AppResource
import com.sayed.newsapp.model.NewsResult
import com.sayed.newsapp.services.NewsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsRepository @Inject constructor(var newsService: NewsService) {

    //get news
    fun getNews(api_key: String): LiveData<AppResource<NewsResult>>{
        val result = MutableLiveData<AppResource<NewsResult>>()
        result.postValue(AppResource.loading(null))
        GlobalScope.launch{ //Start Coroutine in global scope

            try {
                val response=newsService.getNews(api_key).await()
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){ result.postValue(AppResource.success(response.body())) }//In Main Thread
                }else{
                    withContext(Dispatchers.Main){ result.postValue(AppResource.error(null, response.message())) }//In Main Thread
                }

            }catch (e: Exception){
                withContext(Dispatchers.Main){ result.postValue(AppResource.error(null, e.message.toString())) }//In Main Thread
            }
        }
        return result
    }
}