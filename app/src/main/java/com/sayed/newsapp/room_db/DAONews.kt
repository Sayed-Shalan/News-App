package com.sayed.newsapp.room_db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.OnConflictStrategy.ROLLBACK

@Dao
interface DAONews {

    //Queries for news Table

    //insert a news
    @Insert(onConflict = REPLACE) //Conflict strategy replace column
    fun insertNews(entityNews: EntityNews)

    //Update a news
    @Update(onConflict = ROLLBACK) //on conflict not updating any thing
    fun updateNews(entityNews: EntityNews)

    //Delete a news
    @Delete
    fun deleteNews(entityNews: EntityNews)

    //get All News
    @Query("SELECT * FROM news")
    fun getItems(): List<EntityNews>

    //get news by id
    @Query("SELECT * FROM news WHERE id = :m_id")
    fun getNewsById(m_id: Long): List<EntityNews>

    //delete a news by id
    @Query("DELETE FROM news WHERE id= :mID")
    fun deleteNewsById(mID: Long)

    //update news by id
    @Query("UPDATE news SET title=:title,description=:description WHERE id=:id")
    fun updateNewsById(id: Long,title: String, description: String)
}