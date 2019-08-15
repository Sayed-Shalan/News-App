package com.sayed.newsapp.ui.home

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.ActivityMainBinding
import com.sayed.newsapp.ui.base.HasSupportFragmentInjectorActivity
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager

import android.view.Gravity
import android.view.Menu
import android.widget.SearchView
import android.widget.TextView
import com.facebook.AccessToken
import com.sayed.newsapp.managers.BroadcastManager
import com.sayed.newsapp.other.OkCancelCallback
import com.sayed.newsapp.utils.AppUtils
import com.sayed.newsapp.utils.SPUtils
import javax.inject.Inject


class MainActivity : HasSupportFragmentInjectorActivity(), NavController.OnDestinationChangedListener {

    //Dec Data
    lateinit var binding: ActivityMainBinding
    lateinit var navController : NavController
    lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var spUtil: SPUtils


    /**
     * START ON CREATE ****************************
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        initToolbar(binding.toolbar) //Set toolbar
        initData() //init data
    }


    //init data
    private fun initData() {
        initDrawerLayout()
        navController= Navigation.findNavController(this, R.id.main_nav_host_fragment)
        navController.addOnDestinationChangedListener(this)
        binding.navigationView.setNavigationItemSelectedListener(onNavigation)

    }

    //init toggle btn and typeface for nav view
    private fun initDrawerLayout() {

        //init toggle btn
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)
        binding.drawerLayout.addDrawerListener(toggle)
        binding.drawerLayout.post({ toggle.syncState() })

    }

    //On Nav host destination changed
    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        binding.appBar.visibility=if(destination.id==R.id.loginFragment) View.GONE else View.VISIBLE // hide/show app bar aligned with login fragment
    }

    /**
     * ON OPTION ITEM SELECTED ***************************************
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toggle.onOptionsItemSelected(item)
    }

    /**
     * ON CREATE OPTION MENU
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu) //Inflate menu
        val searchItem= menu!!.findItem(R.id.action_search) //get search item
        val searchView= searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(searchListener)
        val id = searchView.context
            .resources
            .getIdentifier("android:id/search_src_text", null, null)

        val searchTxtView= searchView.findViewById(id) as TextView
        searchTxtView.setTextColor(Color.WHITE);
        return super.onCreateOptionsMenu(menu)
    }

    //Send broadcast for new char
    private fun sendLocalBroadcast(newText: String?) {
        val intent = Intent(BroadcastManager.ACTION_NEW_CHAR)
        intent.putExtra(BroadcastManager.ARG_CHAR,newText)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    /**
     * LISTENERS ***************************************************
     */
    var onNavigation= object : NavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            binding.drawerLayout.closeDrawer(Gravity.START)
            item.isChecked = false
            if (item.itemId==R.id.action_logout){

                AppUtils.buildOkCancelDialog(this@MainActivity,
                    resources.getString(R.string.are_you_sure_you_want_to_logout),
                    resources.getString(R.string.ok),
                    resources.getString(R.string.cancel),
                    object : OkCancelCallback {
                        override fun onOkClick() {
                            spUtil.clear()
                            AccessToken.setCurrentAccessToken(null)
                            Navigation.findNavController(this@MainActivity,R.id.main_nav_host_fragment).navigate(R.id.action_homeFragment_to_loginFragment)
                        }

                        override fun onCancelClick() {}
                    })

            }
            return true
        }
    }

    var searchListener=object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            sendLocalBroadcast(newText)
            return true
        }

    }



}
