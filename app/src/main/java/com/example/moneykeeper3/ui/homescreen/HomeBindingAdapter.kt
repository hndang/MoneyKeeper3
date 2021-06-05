package com.example.moneykeeper3.ui


import android.icu.text.CompactDecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.ULocale
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneykeeper3.*
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.ui.homescreen.FilterAdapter
import com.example.moneykeeper3.ui.homescreen.GraphAdapter
import com.example.moneykeeper3.ui.homescreen.HomeFragment
import com.example.moneykeeper3.ui.homescreen.TransactionListAdapter
import com.example.moneykeeper3.ui.transactionscreen.CategoryListAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import kotlin.math.abs


@BindingAdapter("bindIcon")
fun bindIcon(imgView: ImageView, categoryIcon: String?){

    val resource = defaultIcon[categoryIcon] ?: defaultIcon["ic_unknown"]
    resource?.let{
        imgView.setImageResource(resource)
    }

}

@BindingAdapter("setTransactionDate")
fun setTransactionDate(txtView: TextView, date: Long?){
    date?.let{
        txtView.text = formatDatePretty(getDateFromRepo(date))
    }
}
@BindingAdapter(value = ["setMoney", "maxMoneyDigit", "expenseColor", "incomeColor", "defaultColor"], requireAll = false)
fun setTransactionAmt(txtView: TextView, amt: Double?, maxDigit: Int?, expenseColor: Int?, incomeColor: Int?, defaultColor: Int?){

    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(ULocale.getDefault())
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val compactCurrencyFormat: CompactDecimalFormat = CompactDecimalFormat.getInstance(ULocale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT)

    val max = maxDigit ?: 15

    amt?.let{ amount ->
        if(amount.toString().length > max) {
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

@BindingAdapter(value = ["listData","dataUnfiltered"], requireAll = false)
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CategoryAndTransaction>?, dataUnfiltered: List<CategoryAndTransaction>?){
    val concatAdapter = recyclerView.adapter as ConcatAdapter
    val filterAdapter = concatAdapter.adapters[0] as FilterAdapter
    val transactionListAdapter = concatAdapter.adapters[1] as TransactionListAdapter

    if (data.isNullOrEmpty()) {
        Timber.d("Data is null or empty")
        listOf(emptyCategoryAndTransaction)
        transactionListAdapter.submitList(null)
    }
    else {
        transactionListAdapter.submitList(data)
        Timber.d("Submitting data")
    }

    if (!dataUnfiltered.isNullOrEmpty()){
        val categorySet = mutableSetOf<Category>()
        for(item in dataUnfiltered){
            categorySet.add(item.category)
        }
        Timber.d("submitting $categorySet")
        filterAdapter.submitList(listOf(categorySet.toList()))
        //filterAdapter.updateCategory(categorySet.toList())
    }else{
        filterAdapter.submitList(listOf(defaultCategory))
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
                categoryFilter(this, viewModel, category.name)
                text = category.name
            }
            chipGroup.addView(newChip)
        }
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

@BindingAdapter("setCheckButton")
fun setCheckButton(toggleButtonGroup: MaterialButtonToggleGroup, checkedButton: Int?){
    // Binding toggle buttons status to livedata
    if(checkedButton == null){
        toggleButtonGroup.check(R.id.day_filter)
    }else{
        toggleButtonGroup.check(checkedButton)
    }
}
