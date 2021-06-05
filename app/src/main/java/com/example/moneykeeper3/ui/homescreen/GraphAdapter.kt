package com.example.moneykeeper3.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.databinding.HomeFragmentGraphBinding
import com.example.moneykeeper3.model.TransactionViewModel

class GraphAdapter(val viewModel: TransactionViewModel): RecyclerView.Adapter<GraphAdapter.GraphViewHolder>() {
    class GraphViewHolder(private val binding: HomeFragmentGraphBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){

        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        val binding = HomeFragmentGraphBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel

        return GraphViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        holder.bind()
    }

}