package com.example.moneykeeper3.ui.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.databinding.FragmentHomeBinding
import com.example.moneykeeper3.databinding.RvTransactionItemBinding
import com.example.moneykeeper3.model.TransactionViewModel

class TransactionListAdapter : ListAdapter<CategoryAndTransaction, TransactionListAdapter.TransactionViewHolder>(DiffCallback) {
    class TransactionViewHolder(private val binding: RvTransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryAndTransaction: CategoryAndTransaction){
            binding.transaction = categoryAndTransaction.transaction
            binding.category = categoryAndTransaction.category
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<CategoryAndTransaction>(){
        override fun areItemsTheSame(oldItem: CategoryAndTransaction, newItem: CategoryAndTransaction): Boolean {
            return oldItem.transaction.transactionId == newItem.transaction.transactionId
        }

        override fun areContentsTheSame(oldItem: CategoryAndTransaction, newItem: CategoryAndTransaction): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = RvTransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val categoryAndTransaction = getItem(position)
        holder.bind(categoryAndTransaction)
    }

}