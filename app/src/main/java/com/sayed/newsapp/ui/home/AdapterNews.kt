package com.sayed.newsapp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.ListItemNewsBinding
import com.sayed.newsapp.model.News
import com.sayed.newsapp.ui.base.BaseAdapter
import com.sayed.newsapp.ui.base.DataBindingViewHolder
import com.sayed.newsapp.utils.AppUtils

 class AdapterNews(var items: List<News>,var onItemClickListener: ItemClickCallback): BaseAdapter<AdapterNews.NewsViewHolder>() {


    //On create
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context),parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(items.get(position),position)
    }

     //refresh
     fun refresh(newItems: List<News>){
         this.items=newItems
         notifyDataSetChanged()
     }

    /**
     * CREATE VIEW HOLDER CLASS ************
     */
    inner class NewsViewHolder//constructor
    internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        DataBindingViewHolder<ListItemNewsBinding>(inflater, parent, R.layout.list_item_news) {

        //on data
        @SuppressLint("SetTextI18n")
        internal fun onBind(news: News, position: Int) {

            //set image with caching
            Glide.with(context)
                .load(news.urlToImage)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.ivNews)
            binding.tvTitle.setText(news.title)
//            binding.tvDate.setText(AppUtils.convertDateToClientTimeZone(news.publishedAt))
            binding.tvDate.setText(news.publishedAt.substring(0,10)+" "+news.publishedAt.substring(11,16))
            binding.cardView.setOnClickListener { onItemClickListener.onItemClick(position,news) }

        }
    }

    //on click listener
    interface ItemClickCallback {
        fun onItemClick(position: Int, news: News) //on edit pickup click

    }
}