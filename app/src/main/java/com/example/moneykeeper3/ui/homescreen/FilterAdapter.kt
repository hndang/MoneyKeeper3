package com.example.moneykeeper3.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.R
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.databinding.HomeFragmentFilterBinding
import com.example.moneykeeper3.defaultCategory
import com.example.moneykeeper3.model.TransactionViewModel

import com.google.android.material.chip.Chip
import timber.log.Timber

class FilterAdapter(private val viewModel: TransactionViewModel): ListAdapter< List<Category>, FilterAdapter.FilterViewHolder>(DiffCallback) {

    private lateinit var binding: HomeFragmentFilterBinding
    private var checkedButton: Int? = null // This is for bug when clicking holderview is recreated and reset button

    class FilterViewHolder(private val binding: HomeFragmentFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    companion object DiffCallback : DiffUtil.ItemCallback<List<Category>>() {
        override fun areItemsTheSame(oldItem: List<Category>, newItem: List<Category>): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: List<Category>, newItem: List<Category>): Boolean {
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        binding = HomeFragmentFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel



        binding.filterButton.addOnButtonCheckedListener() { group, checkedId, isChecked ->
            if (isChecked) {
                binding.chipGroup.visibility = View.VISIBLE
            } else {
                binding.chipGroup.visibility = View.GONE
                binding.chipGroup.clearCheck()
            }
        }

        binding.dateFilter.addOnButtonCheckedListener() { group, checkedId, isChecked ->
            Timber.d("datefilter: $group, $checkedId, $isChecked")
            viewModel.updateDateRangeName(checkedId)
            binding.executePendingBindings()
            if (isChecked) {
                when (checkedId) {
                    R.id.day_filter -> viewModel.getTransactionToday()
                    R.id.week_filter -> viewModel.getTransactionThisWeek()
                    R.id.month_filter -> viewModel.getTransactionThisMonth()
                    R.id.year_filter -> viewModel.getTransactionThisYear()
                    // todo range filter
                }
            }
        }
        Timber.d("Crate viewhodler")
        return FilterViewHolder(binding)
    }

    fun updateCategory(categories: List<Category>) {
        binding.chipGroup.removeAllViews()
        for (category in categories) {
            val newChip = newChip(binding.chipGroup)
            newChip.apply {
                categoryFilter(this, viewModel, category.name)
                text = category.name
            }
            binding.chipGroup.addView(newChip)
        }
    }

    fun categoryFilter(chip: Chip, viewModel: TransactionViewModel, filterName: String){
        chip.isChecked = viewModel.categoryFilter.value?.contains(filterName) ?: false
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                viewModel.addCategoryFilter(filterName)
            }else{
                viewModel.removeCategoryFilter(filterName)
            }
        }
    }

    fun newChip(view: View) : Chip{
        return LayoutInflater.from(view.context).inflate(R.layout.standalone_chip, view as ViewGroup, false) as Chip
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val category = getItem(0)
        if(category == defaultCategory){
            Timber.d("No data, set empty view")
        }else{
            updateCategory(getItem(0))
        }

    }

    override fun onCurrentListChanged(previousList: MutableList<List<Category>>, currentList: MutableList<List<Category>>) {
        super.onCurrentListChanged(previousList, currentList)
        if(previousList.isNotEmpty()){
            updateCategory(currentList[0])
        }
    }



}