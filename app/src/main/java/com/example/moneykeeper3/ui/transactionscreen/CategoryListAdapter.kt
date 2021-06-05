package com.example.moneykeeper3.ui.transactionscreen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.moneykeeper3.R
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.databinding.CategoriesListBinding
import timber.log.Timber


class CategoryListAdapter(context: Context, var categories : MutableList<Category> ) : ArrayAdapter<Category>(context, 0, categories) {
    class CategoryListViewHolder (val view: View, val binding: CategoriesListBinding){
        init {
            view.tag = this
        }
        fun bind(category: Category?){
            binding.category = category
            binding.executePendingBindings()
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder =
            if (convertView != null) {
                (convertView.tag as CategoryListViewHolder )
            }else {
                //TODO fix
                val binding = CategoriesListBinding.inflate(LayoutInflater.from(context), parent, false)
                CategoryListViewHolder(binding.root, binding)
            }
            holder.bind(getItem(position))
        return holder.view
    }

    override fun getFilter(): Filter {
        return categoryFilter
    }

    val categoryFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Timber.d("filter $categories")
            val result: FilterResults = FilterResults()
            result.values = categories
            result.count = categories.size
            return result
//            if (constraint == null || constraint.length == 0) {
//                result.values =
//            }
            //TODO: Future if want to add actual filtering
//            else {
//
//            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results?.values as List<Category>)
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Category).name
        }
    }

    fun setCategory(newList: List<Category>){
        clear()
        addAll(newList)

    }


}