package com.sayed.newsapp.ui.home


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.sayed.newsapp.R
import com.sayed.newsapp.api.AppResource
import com.sayed.newsapp.databinding.FragmentNewsBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.managers.BroadcastManager
import com.sayed.newsapp.model.News
import com.sayed.newsapp.model.NewsResult
import com.sayed.newsapp.ui.base.BaseFragment
import com.sayed.newsapp.ui.base.BaseViewModel
import com.sayed.newsapp.utils.AppUtils
import com.sayed.newsapp.utils.SPUtils
import com.sayed.newsapp.view_model.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NewsFragment : BaseFragment(), Injectable{

    //Dec Data
    lateinit var adapterNews: AdapterNews
    lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    var items= arrayListOf<News>()
    var tempItems= arrayListOf<News>()


    @Inject
    lateinit var spUtils: SPUtils

    /**
     * ON CREATE VIEW ****************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_news, container, false)
        return binding.root
    }

    /**
     * ON VIEW CREATED ***************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(NewsViewModel::class.java) //init view Model
        viewModel.observeNews().observe(viewLifecycleOwner, Observer { handleNewsResult(it) })
        initData()

    }

    /**
     * ON RESUME ***********************************
     */
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this.activity!!).registerReceiver(receiver, IntentFilter(BroadcastManager.ACTION_NEW_CHAR))
    }

    /**
     * ON STOP **************************************
     */
    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(receiver)
    }

    //init data
    private fun initData() {
       setAdapter() //Bind adapter with recycler

       if (AppUtils.isNetworkAvailable(this.activity!!))pullNews() //call view model to get data
        else if (spUtils.list!=null) handleNewsResult(AppResource.success(NewsResult("",20, spUtils.list!!)),false)

       binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
    }

    //set adapter
    private fun setAdapter(){
        adapterNews= AdapterNews(items,itemCallBack)
        binding.recyclerView.layoutManager=GridLayoutManager(activity,2)
        binding.recyclerView.adapter=adapterNews
        adapterNews.notifyDataSetChanged()
    }

    //pull data
    private fun pullNews(){
        viewModel.pullNews(resources.getString(R.string.api_key))
    }

    //go to description
    fun goToDescription(news: News){
        val action = NewsFragmentDirections.actionNewsFragmentToDetailFragment()
        action.newsArgs=news

        NavHostFragment.findNavController(this).navigate(action)
//        Navigation.findNavController(activity!!,R.id.main_nav_host_fragment).navigate(action)
    }

    //handle Result
    private fun handleNewsResult(appResource: AppResource<NewsResult>?,update: Boolean=true){
        if (appResource!!.isLoading()){  //LOADING
            binding.swipeRefresh.isRefreshing=true
            return
        }
        binding.swipeRefresh.isRefreshing=false
        if (appResource.isError()){ // ERROR
            val errMsg=if (appResource.error!=null) appResource.error else resources.getString(R.string.error_occurred)
            showSnackMsg(errMsg!!,binding.swipeRefresh)
            return
        }

        if (appResource.isSuccessfully()){ //SUCCESS
            if (appResource.data!=null){
                tempItems.clear()
                tempItems.addAll(appResource.data!!.articles)
                adapterNews.refresh(appResource.data!!.articles)
                if (update) spUtils.list=appResource.data!!.articles
            }
        }
    }

    //Search News
    private fun searchItems(newsChar: String) {

        GlobalScope.launch(Dispatchers.Default){// as it heavy operation on cpu  --> Croutine
            items.clear()
            for (news in tempItems){

                if (news.title.startsWith(newsChar,true)
                    || news.title.endsWith(newsChar,true)
                    || news.title.contentEquals(newsChar)
                    || news.description.startsWith(newsChar,true)
                    || news.description.endsWith(newsChar,true)
                    || news.description.contentEquals(newsChar)){
                    items.add(news)

                }
            }

            withContext(Dispatchers.Main){
                adapterNews.refresh(items)
            }
        }
    }

    /**
     * Listeners ***************************************************
     */
    var onRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        pullNews()
    }

    var itemCallBack=object : AdapterNews.ItemClickCallback {
        override fun onItemClick(position: Int, news: News) {
            goToDescription(news)
        }
    }

    /**
     * RECEIVERS ***************************************************
     */
     val receiver=object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent==null||intent.extras==null) return
            val newChar=intent.extras!!.getString(BroadcastManager.ARG_CHAR)

            if (newChar==null) return
            if (tempItems.size==0) return

            searchItems(newChar)
        }

    }



}
