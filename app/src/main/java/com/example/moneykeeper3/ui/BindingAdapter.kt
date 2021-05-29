package com.example.moneykeeper3.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.defaultIcon
import com.example.moneykeeper3.ui.homescreen.TransactionListAdapter


@BindingAdapter("bindIcon")
fun bindIcon(imgView: ImageView, categoryIcon: String?){
    categoryIcon?.let{
        val resource = defaultIcon[categoryIcon] ?: defaultIcon["ic_unknown"]
        resource?.let{
            imgView.setImageResource(resource)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CategoryAndTransaction>?){
    val adapter = recyclerView.adapter as TransactionListAdapter
    adapter.submitList(data)
}
