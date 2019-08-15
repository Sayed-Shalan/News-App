package com.sayed.newsapp.ui.home


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.facebook.AccessToken

import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.FragmentLocalBinding
import com.sayed.newsapp.databinding.FragmentNewsDetailBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.other.OkCancelCallback
import com.sayed.newsapp.room_db.EntityNews
import com.sayed.newsapp.ui.base.BaseFragment
import com.sayed.newsapp.utils.AppUtils
import com.sayed.newsapp.view_model.LocalNewsViewModel
import javax.inject.Inject

class LocalFragment : BaseFragment(),Injectable {

    //dec data
    lateinit var binding: FragmentLocalBinding
    lateinit var adapter : AdapterLocalNews
    lateinit var viewModel: LocalNewsViewModel
    var items= arrayListOf<EntityNews>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    /**
     * On Create View ***********************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_local, container, false)
        return binding.root
    }

    /**
     * On View Created **********************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(LocalNewsViewModel::class.java) //init view Model
        initData() // Init data
    }

    //init data
    private fun initData() {
        binding.fabBtn.setOnClickListener { goToAddNews() } //on click add btn

        setAdapter()
        viewModel.observeNews().observe(viewLifecycleOwner, Observer { observeNews(it!!) })
        viewModel.pullNews() //get news

        //add LD observers
        viewModel.observeDelete().observe(this, Observer { Log.e("DELETE","Success") })
    }

    //set up adapter
    private fun setAdapter() {
        adapter=AdapterLocalNews(items,localItemListener)
        binding.recyclerView.layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.adapter=adapter
        adapter.notifyDataSetChanged()
    }

    //observe news
    fun observeNews(items : List<EntityNews>){
        if (items.isEmpty()){
            binding.tvNoNews.visibility= View.VISIBLE
            return
        }
        binding.tvNoNews.visibility= View.GONE
        adapter.refresh(items)
    }

    //open add news fragment
    fun goToAddNews() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_addNewsFragment) //open add fragment
    }

    //open add news fragment in edit mode
    fun goToAddNews(news:EntityNews){
        val action = HomeFragmentDirections.actionHomeFragmentToAddNewsFragment()
        action.edit=news
        NavHostFragment.findNavController(this).navigate(action) //open add fragment in edit mode
    }

    /**
     * Listeners **********************
     */
    var localItemListener=object : AdapterLocalNews.LocalNewsCallback{

        //On edit item
        override fun onEditNewsClick(position: Int, news: EntityNews) {
           goToAddNews(news)
        }

        //On Delete item
        override fun onDeleteNewsClick(position: Int, news: EntityNews) {
            AppUtils.buildOkCancelDialog(
                activity!!,
                resources.getString(R.string.confirm_delete_item),
                resources.getString(R.string.ok),
                resources.getString(R.string.cancel),
                object : OkCancelCallback {
                    override fun onOkClick() {
                        viewModel.pullDelete(news.id!!)
                        items= adapter.items as ArrayList<EntityNews>
                        items.remove(news)
                        adapter.refresh(items)
                        if (items.isEmpty()) binding.tvNoNews.visibility= View.VISIBLE
                    }

                    override fun onCancelClick() {}
                })
        }
    }


}
