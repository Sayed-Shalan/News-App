package com.sayed.newsapp.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.facebook.login.LoginResult
import com.sayed.newsapp.api.AppResource
import com.sayed.newsapp.repositories.LoginRepository
import com.sayed.newsapp.ui.base.BaseViewModel
import org.json.JSONObject
import javax.inject.Inject

class LoginViewModel @Inject constructor(loginRepository: LoginRepository) : BaseViewModel(){

    //dec data - lifeData
    private val resultLoginLD: LiveData<AppResource<JSONObject>>
    private val pullLoginLD= MutableLiveData<LoginResult>()

    //during create object
    init {
        resultLoginLD= Transformations.switchMap(pullLoginLD,loginRepository::getFacebookUser)
    }

    //pull
    public fun pullLogin(loginResult: LoginResult){
        pullLoginLD.postValue(loginResult)
    }

    //observe
    public fun observeLogin(): LiveData<AppResource<JSONObject>> {
        return resultLoginLD
    }

}