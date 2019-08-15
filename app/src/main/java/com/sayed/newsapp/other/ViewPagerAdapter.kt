package com.sayed.newsapp.other

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter constructor(fragmentManager: FragmentManager,var fragmentsList: ArrayList<Fragment>) : FragmentPagerAdapter(fragmentManager) {

    // Returns total number of pages
    override fun getCount(): Int {
        return fragmentsList.size
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment {
        return fragmentsList[position]
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }
}