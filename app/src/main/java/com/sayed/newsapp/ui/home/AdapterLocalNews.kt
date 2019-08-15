package com.sayed.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.ListItemLocalNewsBinding
import com.sayed.newsapp.room_db.EntityNews
import com.sayed.newsapp.ui.base.BaseAdapter
import com.sayed.newsapp.ui.base.DataBindingViewHolder

class AdapterLocalNews(var items:List<EntityNews>,var localNewsCallback: LocalNewsCallback) :
    BaseAdapter<AdapterLocalNews.LocalNewsViewHolder>() {

    //on create View holder
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): LocalNewsViewHolder {
        return LocalNewsViewHolder(LayoutInflater.from(parent.context),parent)
    }

    // return num of items for binding
    override fun getItemCount(): Int {
        return items.size
    }

    //On Bind View Holder
    override fun onBindViewHolder(holder: LocalNewsViewHolder, position: Int) {
        holder.onBinding(items.get(position),position)
    }

    //refresh
    fun refresh(newItems: List<EntityNews>){
        this.items=newItems
        notifyDataSetChanged()
    }

    /*
     *Local News View holder **********************
     */
    inner class LocalNewsViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        DataBindingViewHolder<ListItemLocalNewsBinding>
        (inflater, parent, R.layout.list_item_local_news){

        //On Binding
        fun onBinding(news: EntityNews, position: Int){

            //Set Data
            binding.tvTitle.setText(news.title)
            binding.tvDescription.setText(news.description)


            //Listeners
            binding.swipeLayout.setOnClickListener {
                if (news.swipe) {
                    binding.swipeLayout.close(true)
                    news.swipe=false
                } else {
                    binding.swipeLayout.open(true)
                    news.swipe=true
                }
            }

            //set listener
            binding.frameEdit.setOnClickListener { v ->
                if (localNewsCallback != null)
                    localNewsCallback.onEditNewsClick(position, news)
            }
            binding.frameDelete.setOnClickListener { v ->
                if (localNewsCallback != null)
                    localNewsCallback.onDeleteNewsClick(position, news)
            }
        }
    }

    /**
     * Create Interfaces ***********************
     */
    interface LocalNewsCallback {
        fun onEditNewsClick(position: Int, news: EntityNews) //on edit news click
        fun onDeleteNewsClick(position: Int, news : EntityNews) //on delete news click
    }
}