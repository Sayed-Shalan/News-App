package com.sayed.newsapp.di

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sayed.newsapp.BuildConfig
import com.sayed.newsapp.app.App
import com.sayed.newsapp.other.Const
import com.sayed.newsapp.services.NewsService
import com.sayed.newsapp.utils.AppUtils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory


@Module
public class ServiceModule {

    /**
     *DEFINE RETROFIT - API - Singleton
     */
    @Singleton
    @Provides
    fun provideGson() : Gson{
        return Gson()
    }

    //Retrofit singleton
    @Singleton
    @Provides
    fun provideRetrofitOrders(app: App, gson: Gson) : Retrofit{
        return Retrofit.Builder().
            baseUrl(BuildConfig.HOST).
            addConverterFactory(GsonConverterFactory.create(gson)).
            addCallAdapterFactory(CoroutineCallAdapterFactory()).
            client(getClient(app)).
            build()
    }


    //get ok-http client with cache for 4 weeks - and cache size 10 mb
    private fun getClient(context: App): OkHttpClient {
        val cache = Cache(context.getCacheDir(), Const.RETROFIT_CACHE_SIZE)
        return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
            var request = chain.request()
            if (!AppUtils.isNetworkAvailable(context)) {
                val maxStale = 60 * 60 * 24 * 28 //4 weeks
                request = request.newBuilder().addHeader("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
            chain.proceed(request)

        }.build()
    }

    /**
     * START SERVICES INJECTION
     */
    @Singleton
    @Provides
    internal fun provideNewsService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }
}