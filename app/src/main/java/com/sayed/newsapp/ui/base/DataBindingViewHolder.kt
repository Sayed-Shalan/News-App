package com.sayed.newsapp.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

open class DataBindingViewHolder<Binding : ViewDataBinding> private constructor(var binding: Binding) :
    RecyclerView.ViewHolder(binding.root) {

    protected val context: Context
        get() = binding.root.context

    constructor(parent: ViewGroup, layout: Int) : this(LayoutInflater.from(parent.context), parent, layout) {}

    constructor(inflater: LayoutInflater, parent: ViewGroup, layout: Int) : this(
        DataBindingUtil.inflate<Binding>(
            inflater,
            layout,
            parent,
            false
        )
    ) {
    }


    init {
        initListener()
    }

    protected fun initListener() {}
}
