package com.example.moneykeeper3.model

import androidx.lifecycle.*
import com.example.moneykeeper3.R
import com.example.moneykeeper3.TIME_ZONE_DB
import com.example.moneykeeper3.convertCalendarForRepo
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionRepository
import com.example.moneykeeper3.getDateOfDay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*




class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    //val a =
    val categories: LiveData<List<Category>> = repository.categories.asLiveData()
    // TODO: switch livedata to different value
    val dateToday: LiveData<List<CategoryAndTransaction>> = repository.getTransactionByDateRange(getDateOfDay(0)).asLiveData()
    //val dateToday : LiveData<List<CategoryAndTransaction>> = repository.test.asLiveData()
    //private val dateMonth: LiveData<List<Transaction>> = repository.getTransactionByDateRange(1,1).asLiveData()
    //private val dateYear: LiveData<List<Transaction>> = repository.getTransactionByDateRange(1,1).asLiveData()
    // TODO: move unknonwCategory to the Category class and fix drawable
    val unknownCategory = Category("Unknown",0x8eacbb, "ic_unknown")
    val testT = Transaction(0,20210529090000,33.0,"Unknown", "note")
    val test : List<CategoryAndTransaction> = listOf(CategoryAndTransaction(testT,unknownCategory))

    fun newCategory(category: Category) = viewModelScope.launch {
        repository.newCategory(category)
    }

    fun newTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.newTransaction(transaction)
    }

    fun getCategoryByName(name: String) : Category{
        return categories.value?.firstOrNull{it.name == name} ?: unknownCategory
    }

}

class TransactionViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

