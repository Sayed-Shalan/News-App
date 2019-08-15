package com.sayed.newsapp.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.util.Log
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginResult
import com.sayed.newsapp.api.AppResource
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor() {

    //get user data from facebook
    fun getFacebookUser(loginResult: LoginResult) : LiveData<AppResource<JSONObject>> {
        val resultLifeData= MutableLiveData<AppResource<JSONObject>>()
        resultLifeData.postValue(AppResource.loading(null)) //send loading status
        val request: GraphRequest = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { jsonObject: JSONObject, graphResponse: GraphResponse ->
            Log.e("User Data",jsonObject.toString())
            resultLifeData.postValue(AppResource.success(jsonObject))
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,first_name,last_name,email")
        request.setParameters(parameters)
        request.executeAsync()
        return resultLifeData
    }
}