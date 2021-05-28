package com.example.moneykeeper3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.CookieHandler

@Database(
    entities = [Transaction::class, Category::class],
    version = 1,
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

            // Add demo transaction
        }
    }
}