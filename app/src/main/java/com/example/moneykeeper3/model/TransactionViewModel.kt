package com.example.moneykeeper3.model


import android.util.Log
import androidx.lifecycle.*
import com.example.moneykeeper3.DateRange
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionRepository
import com.example.moneykeeper3.defaultCategory
import com.example.moneykeeper3.getDateOfDay
import com.example.moneykeeper3.getDateOfMonth
import kotlinx.coroutines.launch


class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    // Range for Room query
    private var _dateRangeFilter = MutableLiveData<DateRange>()
    val dateRangeFilter : LiveData<DateRange> get() = _dateRangeFilter

    // Category for Room query
    private val _categoriesFilter = MutableLiveData<MutableSet<String>>()
    val categoryFilter : LiveData<MutableSet<String>> get() = _categoriesFilter

    val filters = MediatorLiveData<Pair<DateRange?, Set<String>?>>().apply {
        Log.d("TEST", "filter applied")
        addSource(_categoriesFilter){
            value = Pair(dateRangeFilter.value, it)
        }
        addSource(_dateRangeFilter){
            value = Pair(it, categoryFilter.value)
        }
    }

    val transactions: LiveData<List<CategoryAndTransaction>> = Transformations.switchMap(filters){ pair ->
        val dateRange = pair.first
        val categoryFilter = pair.second
        Log.d("TEST", "$dateRange and $categoryFilter")
        if (dateRange != null){
            if (categoryFilter != null){
                repository.getTransactionWithFilter(dateRange, categoryFilter.toList()).asLiveData()
            }else{
                repository.getTransactionWithFilter(dateRange, listOf()).asLiveData()
            }
        }else{
            null
        }
    }

    val totalMoney : LiveData<Double> = Transformations.map(transactions){
        var total = 0.0
        for (item in it){
            total += item.transaction.transactionAmount
        }
        total
    }

    //val categories: LiveData<List<Category>> = repository.categories.asLiveData()
    val categories : List<Category> = defaultCategory

    /**
     * Display status immediately today's transaction by default
     */
    init {
        getTransactionToday()
        _categoriesFilter.value = mutableSetOf()
    }


    // Transaction functions
    fun getTransactionToday(){
        _dateRangeFilter.value = getDateOfDay(0)
    }

    fun getTransactionThisMonth(){
        _dateRangeFilter.value = getDateOfMonth(0)
    }

    fun newTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.newTransaction(transaction)
    }

    // Category functions
    fun addCategoryFilter(categoryName: String){
        _categoriesFilter.value?.add(categoryName)
        _categoriesFilter.value = _categoriesFilter.value
    }

    fun removeCategoryFilter(categoryName: String){
        _categoriesFilter.value?.remove(categoryName)
        _categoriesFilter.value = _categoriesFilter.value
    }

    fun newCategory(category: Category) = viewModelScope.launch {
        repository.newCategory(category)
    }

//    fun getCategoryByName(name: String) : Category{
//        return categories.value?.firstOrNull{it.name == name} ?: defaultCategory[0]
//    }

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

