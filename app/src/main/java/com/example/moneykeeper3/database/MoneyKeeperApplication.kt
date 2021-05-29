package com.example.moneykeeper3.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

// To keep one instance of stuffs in the app
class MoneyKeeperApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { TransactionDatabase.getDatabase(this, applicationScope)}
    val repository by lazy {TransactionRepository(database.transactionDao())}
}