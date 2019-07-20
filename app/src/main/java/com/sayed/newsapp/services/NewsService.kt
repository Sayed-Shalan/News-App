package com.sayed.newsapp.services

import com.sayed.newsapp.model.NewsResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface NewsService {
    //get news
    @GET("v2/top-headlines?country=us")
    fun getNews(@Query("apiKey") apiKey: String): Deferred<Response<NewsResult>>
}