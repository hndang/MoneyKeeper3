package com.example.moneykeeper3.database

import androidx.annotation.WorkerThread
import com.example.moneykeeper3.DateRange
import com.example.moneykeeper3.convertCalendarForRepo
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao){
    val categories: Flow<List<Category>> = transactionDao.getCategories()

    fun getTransactionWithFilter(date: DateRange, categories: List<String>) : Flow<List<CategoryAndTransaction>> {
        return if (categories.isNotEmpty()){
            transactionDao.getTransactionWithFilterDistinct(convertCalendarForRepo(date.startDate), convertCalendarForRepo(date.endDate), categories)

        }else{
            transactionDao.getTransactionByDateRangeDistinct(convertCalendarForRepo(date.startDate), convertCalendarForRepo(date.endDate))
        }
    }

    @WorkerThread
    suspend fun deleteTransaction(transaction: Transaction){
        transactionDao.deleteTransaction(transaction)
    }

    @WorkerThread
    suspend fun getTransactionById(id: Long) : Transaction{
        return transactionDao.getTransactionById(id)
    }

    @WorkerThread
    suspend fun newCategory(category: Category){
        transactionDao.insertCategory(category)
    }

    @WorkerThread
    suspend fun newTransaction(transaction: Transaction){
        transactionDao.insertTransaction(transaction)
    }

    @WorkerThread
    suspend fun updateTransaction(transaction: Transaction){
        transactionDao.updateTransaction(transaction)
    }
}