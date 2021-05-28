package com.example.moneykeeper3.ui.homescreen

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.databinding.FragmentHomeBinding
import com.example.moneykeeper3.model.TransactionViewModel

class TransactionListAdapter : ListAdapter<Transaction, TransactionListAdapter.TransactionViewHolder>(DiffCallback) {
    class TransactionViewHolder(val binding: FragmentHomeBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    companion object DiffCallback: DiffUtil.ItemCallback<Transaction>(){
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            TODO("Not yet implemented")
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}