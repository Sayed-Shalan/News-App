package com.sayed.newsapp.ui.home


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment

import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.FragmentAddNewsBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.room_db.EntityNews
import com.sayed.newsapp.ui.base.BaseFragment
import com.sayed.newsapp.view_model.LocalNewsViewModel
import javax.inject.Inject

class AddNewsFragment : BaseFragment(),Injectable {

    //Dec Data
    lateinit var binding : FragmentAddNewsBinding
    lateinit var viewModel: LocalNewsViewModel
    lateinit var news: EntityNews
    var updateMode=false

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    /**
     * ON CREATE VIEW ************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_add_news, container, false)
        return binding.root
    }

    /**
     * On View Created ***************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(LocalNewsViewModel::class.java) //init view Model
        initData() //init Data
    }

    //Init Data
    private fun initData() {
        if (arguments!=null&&AddNewsFragmentArgs.fromBundle(arguments!!).edit!=null){
            setUpViewsForUpdate()
        }
        binding.btnAdd.setOnClickListener(onAddClickListener)
    }

    //Set Views as in update mode
    private fun setUpViewsForUpdate() {
        news=AddNewsFragmentArgs.fromBundle(arguments!!).edit!!
        binding.etTitle.setText(news.title)
        binding.etDescription.setText(news.description)
        binding.btnAdd.setText(resources.getString(R.string.update))
        updateMode=true
    }

    //open Home Page
    private fun goToHome() {
        val action = AddNewsFragmentDirections.actionAddNewsFragmentToHomeFragment()
        action.local=true
        NavHostFragment.findNavController(this).navigate(action)
    }

    /**
     * Listeners ********************************
     */
    val onAddClickListener = object :View.OnClickListener{
        override fun onClick(v: View?) {
            if (binding.etDescription.text.isEmpty()||binding.etTitle.text.isEmpty()) return
            val entityNews=EntityNews()
            entityNews.description=binding.etDescription.text.toString()
            entityNews.title=binding.etTitle.text.toString()
           if (!updateMode) viewModel.pullInsert(entityNews)
            else {
               entityNews.id=news.id
               viewModel.pullUpdate(entityNews)
           }
            goToHome()
        }
    }

}
