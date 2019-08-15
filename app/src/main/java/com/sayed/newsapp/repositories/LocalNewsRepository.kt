package com.sayed.newsapp.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sayed.newsapp.room_db.DAONews
import com.sayed.newsapp.room_db.EntityNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNewsRepository @Inject constructor(var daoNews: DAONews) {

    //get All News
    fun getAllNews() : LiveData<List<EntityNews>>{ //Use coroutines to run in new thread
        val resultLifeData= MutableLiveData<List<EntityNews>>()
        resultLifeData.postValue(daoNews.getItems())
        return resultLifeData
    }

    //insert news
    fun insertNews(entityNews: EntityNews){
        GlobalScope.launch(Dispatchers.Default) {
            daoNews.insertNews(entityNews)
        }
    }

    //delete news
    fun deleteNews(entityNewsId: Long){
        GlobalScope.launch {
            daoNews.deleteNewsById(entityNewsId)
        }
    }

    //update news
    fun updateNews(entityNews: EntityNews){
        GlobalScope.launch {
            daoNews.updateNewsById(entityNews.id!!,entityNews.title,entityNews.description)
        }
    }
}