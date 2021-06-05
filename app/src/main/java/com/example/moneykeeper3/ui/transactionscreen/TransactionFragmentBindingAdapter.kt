package com.example.moneykeeper3.ui.transactionscreen

import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.formatDatePretty
import com.example.moneykeeper3.formatTimePretty
import com.example.moneykeeper3.getDateFromRepo
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import timber.log.Timber
import kotlin.math.abs

@BindingAdapter("setSignTransactionFragment")
fun setSign(switch: SwitchMaterial, money: Double?){
    money?.let{
        if(it != 0.0)
            switch.isChecked = it<0
    }

}

@BindingAdapter("setMoneyTransactionFragment")
fun setMoney(editText: TextInputEditText, money: Double?){
    money?.let{
        if(it != 0.0)
            editText.setText(abs(it).toString())
    }
}

@BindingAdapter("listCategoryTransactionFragment")
fun bindCategoryList(autoTextField: AutoCompleteTextView, categories: List<Category>?){
    categories?.let {
        val adapter = autoTextField.adapter as CategoryListAdapter
        adapter.setCategory(categories)
    }
}

@BindingAdapter("setCategoryTransactionFragment")
fun setCategory(autoTextField: AutoCompleteTextView, category: String?){
    category?.let{
        if(it != "")
            autoTextField.setText(it, false)
    }
}

@BindingAdapter("setNoteTransactionFragment")
fun setNote(editText: TextInputEditText, note: String?){
    note?.let {
        if(it != "")
            editText.setText(it)
    }
}

@BindingAdapter("setDateTransactionFragment")
fun setDate(textView: MaterialTextView, dateTime: Long?){

    dateTime?.let {
        textView.text = formatDatePretty(getDateFromRepo(it))
    }

}

@BindingAdapter("setTimeTransactionFragment")
fun setTime(textView: MaterialTextView, dateTime: Long?){
    dateTime?.let {
        textView.text = formatTimePretty(getDateFromRepo(it).toLocalTime())
    }

}