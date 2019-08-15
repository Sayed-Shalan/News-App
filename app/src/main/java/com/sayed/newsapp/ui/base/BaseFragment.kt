package com.sayed.newsapp.ui.base


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.sayed.newsapp.R
import com.sayed.newsapp.di.Injectable
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    //On create
    override fun onCreate(savedInstanceState: Bundle?) {
        if (this is Injectable) { //decide injected or not
            AndroidSupportInjection.inject(this)
        }

        super.onCreate(savedInstanceState)
    }


    //show snackbar
    fun showSnackMsg(msg: String, view: View?) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setActionTextColor(resources.getColor(R.color.white))
                .setAction("Action", null).show()

        }
    }

    //Change swipe refresh color
    fun setSwipeRefreshColors(refreshLayout: SwipeRefreshLayout) {
        val c1 = resources.getColor(R.color.seamlab_theme)
        val c2 = resources.getColor(R.color.dark_gray_alpha)
        val c3 = resources.getColor(R.color.black)

        refreshLayout.setColorSchemeColors(c1, c2, c3)
    }

}
