package com.sayed.newsapp.ui.home


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.FragmentHomeBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.other.ViewPagerAdapter
import com.sayed.newsapp.ui.base.BaseFragment
import java.util.ArrayList



class HomeFragment : BaseFragment(),Injectable {

    //Dec vars
    lateinit var binding : FragmentHomeBinding
    lateinit var pagerAdapter: ViewPagerAdapter
    lateinit var newsFragment: NewsFragment
    lateinit var localFragment: LocalFragment
    lateinit var fragmentsList: ArrayList<Fragment>

    /**
     * On Create view ****************************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, com.sayed.newsapp.R.layout.fragment_home, container, false)
        return binding.root
    }

    /**
     * On View Created *****************************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData() //set data
    }

    //Set Data
    private fun initData() {
        newsFragment= NewsFragment()
        localFragment= LocalFragment()
        fragmentsList=ArrayList()

        setUpViewPager() //set view pager with tab layout

        checkPage() //if need to open local

    }

    //check if args to open local is true
    private fun checkPage() {
        if (HomeFragmentArgs.fromBundle(this.arguments!!).local) binding.viewPager.currentItem = 1 //  Is local page Index
    }

    //Set up view pager
    private fun setUpViewPager() {
        fragmentsList.add(newsFragment)
        fragmentsList.add(localFragment)
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragmentsList)

        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        //set tabs titles
        binding.tabLayout.getTabAt(0)!!.setText(resources.getString(R.string.recent_news))
        binding.tabLayout.getTabAt(1)!!.setText(resources.getString(R.string.local_news))
    }


}
