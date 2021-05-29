package com.example.moneykeeper3.database

import androidx.room.*


@Entity(tableName = "transactionTable", foreignKeys = [ForeignKey(entity = Category::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("categoryName"),
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE)])
data class Transaction(
        @PrimaryKey(autoGenerate=true) val transactionId: Long = 0,
        val transactionDate: Long,
        val transactionAmount: Double,
        val categoryName: String = "Unknown",
        val transactionNote: String,
)

@Entity(tableName = "categoryTable")
data class Category(
        @PrimaryKey val name: String,
        val categoryColor: Int,
        val categoryIcon: String
)

data class CategoryAndTransaction(
        @Embedded val transaction: Transaction,
        @Relation(
                parentColumn = "categoryName",
                entityColumn = "name"
        ) val category: Category
)