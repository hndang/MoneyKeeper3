package com.example.moneykeeper3.database

import androidx.annotation.WorkerThread
import com.example.moneykeeper3.DateRange
import com.example.moneykeeper3.convertCalendarForRepo
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao){
    val categories: Flow<List<Category>> = transactionDao.getCategories()
//    fun getTransactionByDateRange(date: DateRange): Flow<List<CategoryAndTransaction>> =
//        transactionDao.getTransactionByDateRangeDistinct(convertCalendarForRepo(date.startDate).toLong(), convertCalendarForRepo(date.endDate).toLong())

    fun getTransactionWithFilter(date: DateRange, categories: List<String>) : Flow<List<CategoryAndTransaction>> {
        return if (categories.isNotEmpty()){
            transactionDao.getTransactionWithFilterDistinct(convertCalendarForRepo(date.startDate).toLong(), convertCalendarForRepo(date.endDate).toLong(), categories)

        }else{
            transactionDao.getTransactionByDateRangeDistinct(convertCalendarForRepo(date.startDate).toLong(), convertCalendarForRepo(date.endDate).toLong())
        }
    }


    @WorkerThread
    suspend fun newCategory(category: Category){
        transactionDao.insertCategory(category)
    }

    @WorkerThread
    suspend fun newTransaction(transaction: Transaction){
        transactionDao.insertTransaction(transaction)
    }
}