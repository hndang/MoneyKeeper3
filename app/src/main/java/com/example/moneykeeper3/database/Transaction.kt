package com.example.moneykeeper3.database

import androidx.room.*

@Entity(tableName = "transactions")
data class Transaction(
        @PrimaryKey(autoGenerate=true) val transactionId: Long,
        val date: Long,
        val amount: Double,
        val categoryId: String,
        val note: String,
)

@Entity(tableName = "category")
data class Category(
        @PrimaryKey val name: String,
        val color: Int,
        val icon: Int
)

// relation db
data class CategoryWithTransaction (
        @Embedded val category: Category,

        @Relation(
                parentColumn = "name",
                entityColumn = "categoryId"
        ) val transactions: List<Transaction>
)