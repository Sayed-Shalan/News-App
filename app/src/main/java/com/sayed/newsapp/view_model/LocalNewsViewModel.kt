package com.sayed.newsapp.view_model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.sayed.newsapp.repositories.LocalNewsRepository
import com.sayed.newsapp.room_db.EntityNews
import com.sayed.newsapp.ui.base.BaseViewModel
import javax.inject.Inject

class LocalNewsViewModel @Inject constructor(localNewsRepository: LocalNewsRepository) : BaseViewModel(){

    //dec data - lifeData
    private val resultNewsLD: LiveData<List<EntityNews>>
    private val pullNewsLD= MutableLiveData<Boolean>()
    private var resultInsertLD : LiveData<Unit>
    private val pullInsert = MutableLiveData<EntityNews>()
    private var resultDeletetLD : LiveData<Unit>
    private val pullDelete = MutableLiveData<Long>()
    private var resultUpdateLD : LiveData<Unit>
    private val pullUpdate = MutableLiveData<EntityNews>()


    init {
        resultNewsLD= Transformations.switchMap(pullNewsLD) { localNewsRepository.getAllNews() } //Results as a list live-data
        resultInsertLD=Transformations.map(pullInsert,localNewsRepository::insertNews)//To Insert
        resultDeletetLD=Transformations.map(pullDelete,localNewsRepository::deleteNews)//To delete item by id
        resultUpdateLD=Transformations.map(pullUpdate,localNewsRepository::updateNews) //To update item

    }

    /**
     * Pull *************************************
     */
     fun pullNews(){
        pullNewsLD.postValue(true)
    }
     fun pullInsert(newsEntityNews: EntityNews){
        pullInsert.postValue(newsEntityNews)
    }
     fun pullDelete(id: Long){
        pullDelete.postValue(id)
    }
     fun pullUpdate(newsEntityNews: EntityNews){
        pullUpdate.postValue(newsEntityNews)
    }

    /**
     * Observe **********************************
     */
     fun observeNews(): LiveData<List<EntityNews>> {
        return resultNewsLD
    }
    fun observeInsert() : LiveData<Unit>{
        return resultInsertLD
    }
    fun observeDelete() : LiveData<Unit>{
        return resultDeletetLD
    }
    fun observeUpdate() : LiveData<Unit>{
        return resultUpdateLD
    }

}