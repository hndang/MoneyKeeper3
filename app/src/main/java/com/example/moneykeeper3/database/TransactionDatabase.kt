package com.example.moneykeeper3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moneykeeper3.R
import com.example.moneykeeper3.convertCalendarForRepo
import com.example.moneykeeper3.defaultCategory
import com.example.moneykeeper3.getDateOfDay

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.CookieHandler
import java.time.LocalDate

@Database(
    entities = [Transaction::class, Category::class],
    version = 1,
    exportSchema = false
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TransactionDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                )
                    .addCallback(TransactionDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

    /*
    * Called when the database is created for the first time. This is called after all the
    * tables are created.
    */
    private class TransactionDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let{
                scope.launch(Dispatchers.IO) {
                    populateDatabase(it.transactionDao())
                }
            }
        }

        suspend fun populateDatabase(transactionDao: TransactionDao){
            // Add new category
            for (category in defaultCategory) {
                transactionDao.insertCategory(category)
            }
            // Add demo transaction
            val today = LocalDate.now().atStartOfDay()
            transactionDao.insertTransaction(Transaction(0,convertCalendarForRepo(today.plusHours(7)).toLong(),33.0,defaultCategory[0].name, "note"))
            transactionDao.insertTransaction(Transaction(0,convertCalendarForRepo(today.plusHours(15)).toLong(),33.0, defaultCategory[4].name, "note"))
        }
    }
}