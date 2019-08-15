package com.sayed.newsapp.ui.login


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

import com.sayed.newsapp.R
import com.sayed.newsapp.api.AppResource
import com.sayed.newsapp.databinding.FragmentLoginBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.model.User
import com.sayed.newsapp.other.Const
import com.sayed.newsapp.other.OkCancelCallback
import com.sayed.newsapp.ui.base.BaseFragment
import com.sayed.newsapp.ui.base.Loading
import com.sayed.newsapp.utils.AppUtils
import com.sayed.newsapp.utils.SPUtils
import org.json.JSONObject
import java.util.Arrays
import javax.inject.Inject

class LoginFragment : BaseFragment(), Injectable, FacebookCallback<LoginResult> {

    //Dec Data
    lateinit var binding: FragmentLoginBinding
    lateinit var callbackManager: CallbackManager
    lateinit var viewModel: LoginViewModel
    private lateinit var loading: Loading
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @Inject
    lateinit var spUtils: SPUtils


    /**
     * On Activity result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
          callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Start on create View ******************************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        return binding.root;    }

    /**
     * On View created *****************************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init view model
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        initData()//init data
    }

    /**
     * ON START ******************
     */
    override fun onStart() {
        super.onStart()
        checkIfLoggedIn()
    }

    //init Data
    private fun initData() {
        callbackManager = CallbackManager.Factory.create()
        binding.btnFacebookLogin.setPermissions(listOf("email", "user_events")) //we need permission to read email and user_event
        binding.btnFacebookLogin.fragment=this
        binding.btnFacebookLogin.registerCallback(callbackManager,this)
        loading = Loading(this.activity!!)
        viewModel.observeLogin().observe(viewLifecycleOwner, Observer { updateViews(it) })
    }


    //Check if user Has session
    private fun checkIfLoggedIn() {
        if (AccessToken.getCurrentAccessToken() != null){
            if (AccessToken.getCurrentAccessToken().isExpired){
                AccessToken.setCurrentAccessToken(null)
            }else{
                goToNext()
            }
        }
    }

    //update View
    private fun updateViews(resource: AppResource<JSONObject>?) {
        if (resource==null) return

        //Show Loading
        if (resource.isLoading()){
            showLoading(true)
            return
        }

        showLoading(false)

        //Show Error
        if (resource.isError()){
            showSnackMsg(resources.getString(R.string.error_occurred) ,binding.btnFacebookLogin)
            return
        }

        //Handle Data
        if (resource.isSuccessfully()) setSPData(resource.data)

    }

    //set Data
    private fun setSPData(data: JSONObject?) {
        if (data==null) return
        val user= User()
        user.first_name=data.getString(Const.FIRST_NAME)
        user.last_name=data.getString(Const.LAST_NAME)
        user.email=data.getString(Const.EMAIL)
        user.id=data.getInt(Const.ID)

        //open ok cancel dialog
        AppUtils.buildOkCancelDialog(
            this.activity!!, resources.getString(R.string.continue_as)+" "+(user.first_name)+" "+(user.last_name)+ "?",
            resources.getString(R.string.ok),
            resources.getString(R.string.cancel),
            object : OkCancelCallback {
                override fun onOkClick() {
                    spUtils.user=user
                    checkIfLoggedIn()
                }

                override fun onCancelClick() {
                    AccessToken.setCurrentAccessToken(null)
                }
            })
    }

    //go next fragment
    private fun goToNext() {
        Navigation.findNavController(this.activity!!,R.id.main_nav_host_fragment).navigate(R.id.action_loginFragment_to_homeFragment) //clear stack and go to login
    }

    //show loading
    fun showLoading(show: Boolean) {
        loading.show(show)
    }

    /**
     * Facebook login Callback methods
     */
    override fun onSuccess(result: LoginResult?) {

        if (result!=null) viewModel.pullLogin(result)
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException?) {
    }


}
