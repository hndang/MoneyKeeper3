package com.example.moneykeeper3.model


import android.util.Log
import androidx.lifecycle.*
import com.example.moneykeeper3.*
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.CategoryAndTransaction
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZonedDateTime
import kotlin.random.Random


class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    // date range name for checkbox
    private var _dateRangeName = MutableLiveData<Int>()
    val dateRangeName : LiveData<Int> get() = _dateRangeName

    // Range for Room query
    private var _dateRangeFilter = MutableLiveData<DateRange>()
    val dateRangeFilter : LiveData<DateRange> get() = _dateRangeFilter

    // Category for Room query
    private val _categoriesFilter = MutableLiveData<MutableSet<String>>()
    val categoryFilter : LiveData<MutableSet<String>> get() = _categoriesFilter

    private val _currentTransaction = MutableLiveData<Transaction>(emptyTransaction)
    val currentTransaction: LiveData<Transaction> get() = _currentTransaction

    val filters = MediatorLiveData<Pair<DateRange?, Set<String>?>>().apply {
        Timber.d("filter applied")
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
        Timber.d("$dateRange and $categoryFilter")
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

    val transactionsDateFilterOnly: LiveData<List<CategoryAndTransaction>> = Transformations.switchMap(dateRangeFilter){ dateRange ->
        if(dateRange != null) {
            repository.getTransactionWithFilter(dateRange, listOf()).asLiveData()
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

    val categories: LiveData<List<Category>> = repository.categories.asLiveData()
    //val categories : List<Category> = defaultCategory

    val categoriesList: LiveData<List<String>> = Transformations.map(categories){ categories ->
        categories.map{it.name}
    }

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

    fun getTransaction(id: Long) = viewModelScope.launch {
        _currentTransaction.value = repository.getTransactionById(id)
    }

    fun resetTransaction(){
        _currentTransaction.value = emptyTransaction
    }

    fun getTransactionThisWeek(){
        _dateRangeFilter.value = getDateOfThisWeek()
    }

    fun getTransactionThisMonth(){
        _dateRangeFilter.value = getDateOfMonth(0)
    }

    fun getTransactionThisYear(){
        _dateRangeFilter.value = getDateOfYear(0)
    }

    fun newTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.newTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }

    fun updateDateRangeName(nameId: Int){
        _dateRangeName.value = nameId
    }

    /*****************************
     *  Category functions
     *****************************/

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

    fun newDummyCategory(){
        val i = (Math.random()*100).toInt()
        newCategory(Category("TEST$i", 0x8eacbb, "ic_unknown"))
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

