package com.example.moneykeeper3.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface TransactionDao {

    // Transaction actions

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactionTable")
    suspend fun deleteAllTransaction()

    @Query("SELECT * FROM transactionTable where transactionDate == :date order by transactionDate DESC")
    fun getTransactionByDate(date: Long) : List<Transaction>

//    @Query("SELECT * FROM transactionTable where transactionDate BETWEEN :startDate AND :endDate order by transactionDate DESC ")
//    fun getTransactionByDateRange(startDate: Long, endDate: Long ) : Flow<List<Transaction>>
    @androidx.room.Transaction
    @Query("SELECT * FROM transactionTable where transactionDate BETWEEN :startDate AND :endDate order by transactionDate DESC ")
    fun getTransactionByDateRange(startDate: Long, endDate: Long ) : Flow<List<CategoryAndTransaction>>

    fun getTransactionByDateRangeDistinct(startDate: Long, endDate: Long ) =
        getTransactionByDateRange(startDate, endDate).distinctUntilChanged()

    @androidx.room.Transaction
    @Query("SELECT * FROM transactionTable where (transactionDate BETWEEN :startDate AND :endDate) AND (categoryName IN (:categories)) order by transactionDate DESC" )
    fun getTransactionWithFilter(startDate: Long, endDate: Long, categories: List<String>): Flow<List<CategoryAndTransaction>>
    fun getTransactionWithFilterDistinct(startDate: Long, endDate: Long, categories: List<String>) =
        getTransactionWithFilter(startDate, endDate, categories).distinctUntilChanged()

//    @androidx.room.Transaction
//    @Query("SELECT * FROM transactionTable where transactionDate BETWEEN :startDate AND :endDate AND categoryName IN ('Food') order by transactionDate DESC" )
//    fun getTransactionWithFilter(startDate: Long, endDate: Long): Flow<List<CategoryAndTransaction>>
//    fun getTransactionWithFilterDistinct(startDate: Long, endDate: Long) =
//        getTransactionWithFilter(startDate, endDate).distinctUntilChanged()

    // Category actions
    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM categoryTable")
    suspend fun deleteAllCategory()

    @androidx.room.Transaction
    @Query("SELECT * FROM categoryTable ORDER BY name ASC ")
    fun getCategories(): Flow<List<Category>>

    // Both
    //@androidx.room.Transaction
    @Query("SELECT * FROM transactionTable where transactionDate BETWEEN :startDate AND :endDate order by transactionDate DESC ")
    suspend fun getTransactionByDateRange2(startDate: Long, endDate: Long ) : List<CategoryAndTransaction>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactionTable ")
    fun getTransactionByDateRange3() : Flow<List<CategoryAndTransaction>>
}