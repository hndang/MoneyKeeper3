package com.example.moneykeeper3.ui


import android.icu.text.CompactDecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.ULocale
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.defaultIcon
import com.example.moneykeeper3.getDateFromRepo
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.formatDatePretty
import com.example.moneykeeper3.ui.homescreen.HomeFragment
import com.example.moneykeeper3.ui.homescreen.TransactionListAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.math.abs


@BindingAdapter("bindIcon")
fun bindIcon(imgView: ImageView, categoryIcon: String?){
    categoryIcon?.let{
        val resource = defaultIcon[categoryIcon] ?: defaultIcon["ic_unknown"]
        resource?.let{
            imgView.setImageResource(resource)
        }
    }
}

@BindingAdapter("setTransactionDate")
fun setTransactionDate(txtView: TextView, date: Long?){
    date?.let{
        txtView.text = formatDatePretty(getDateFromRepo(date.toString()))
    }
}
@BindingAdapter(value = ["setMoney", "expenseColor", "incomeColor", "defaultColor"], requireAll = false)
fun setTransactionAmt(txtView: TextView, amt: Double?, expenseColor: Int?, incomeColor: Int?, defaultColor: Int?){

    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(ULocale.getDefault())
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val compactCurrencyFormat: CompactDecimalFormat = CompactDecimalFormat.getInstance(ULocale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT)

    amt?.let{ amount ->
        if(amount.toString().length > 8) {
            val absAmt = abs(amt)
            txtView.text = if (amount <0) "-$${compactCurrencyFormat.format(absAmt)}" else "$${compactCurrencyFormat.format(absAmt)}"
        }else {
            txtView.text = currencyFormat.format(amount)
        }

        if(incomeColor != null && expenseColor != null){
            if (amount > 0){
                txtView.setTextColor(incomeColor)
            }else{
                txtView.setTextColor(expenseColor)
            }
        }else{
            defaultColor?.let{ color ->
                txtView.setTextColor(color)
            }
        }

    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CategoryAndTransaction>?){
    val adapter = recyclerView.adapter as TransactionListAdapter
    adapter.submitList(data)
}

@BindingAdapter(value = ["viewModel", "filterName"])
fun filterName(chip: Chip, viewModel: TransactionViewModel, filterName: String){
    chip.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked){
            viewModel.addCategoryFilter(filterName)
        }else{
            viewModel.removeCategoryFilter(filterName)
        }
    }
}

@BindingAdapter(value = ["categoryList", "viewModel", "fragment"])
fun categoryList(
    chipGroup: ChipGroup,
    categories: List<Category>?,
    viewModel: TransactionViewModel,
    fragment: HomeFragment
){
    categories?.let {
        for (category in it) {
            val newChip = fragment.newChip(chipGroup)
            newChip.apply {
                filterName(this, viewModel, category.name)
                text = category.name
            }
            chipGroup.addView(newChip)
        }
    }
}

