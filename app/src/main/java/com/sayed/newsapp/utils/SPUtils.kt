package com.sayed.newsapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sayed.newsapp.R
import com.sayed.newsapp.model.News
import com.sayed.newsapp.model.User

class SPUtils(context: Context) {

    //dec data
    private val sp: SharedPreferences
    companion object { //for static objects
        //keys
        private val SP_DATA = "SPUtils.Data"
        private val SP_USER_DATA = "SPUtils.UserData"
    }


    init {
        this.sp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }


    //set and get user
    var user: User?
        get() {
            val data = sp.getString(SP_USER_DATA, null) ?: return null
            return Gson().fromJson<User>(data, object : TypeToken<User>() {

            }.type)
        }
        set(user) {
            val data = Gson().toJson(user)
            sp.edit().putString(SP_USER_DATA, data).apply()
        }

    //set and get list<News>
    var list: List<News>?
        get() {
            val data=sp.getString(SP_DATA,null)
            if (data==null) return null
            return Gson().fromJson(data, object : TypeToken<List<News>>() {
            }.type)

        }
        set(value) {
            sp.edit().putString(SP_DATA,Gson().toJson(value)).apply()
        }

    //clear all data
    fun clear() {
        sp.edit().remove(SP_USER_DATA)
            .apply()
    }


}