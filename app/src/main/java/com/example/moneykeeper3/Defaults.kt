package com.example.moneykeeper3

import com.example.moneykeeper3.database.Category

val defaultCategory = listOf(
        Category("Unknown", 0x8eacbb, "ic_unknown"), // This is the Default when get deleted
        Category("Default", 0xcfcfcf, "ic_default"),
        Category("Clothing", 0xa98274, "ic_clothing"),
        Category("Entertainment", 0x6ec6ff, "ic_entertainment"),
        Category("Food", 0xbef67a, "ic_food"),
        Category("Gift", 0xff6090, "ic_gift"),
        Category("Grocery", 0xff8a50, "ic_grocery"),
        Category("Income", 0x80e27e, "ic_income"),
        Category("Rent", 0xff7961, "ic_rent"),
)
val defaultIcon = mapOf(
    "ic_default" to R.drawable.ic_category_default,
    "ic_clothing" to R.drawable.ic_category_clothing,
    "ic_entertainment" to R.drawable.ic_category_entertainment,
    "ic_food" to R.drawable.ic_category_food,
    "ic_gift" to R.drawable.ic_category_gift,
    "ic_grocery" to R.drawable.ic_category_grocery,
    "ic_income" to R.drawable.ic_category_income,
    "ic_rent" to R.drawable.ic_category_rent,
    "ic_unknown" to R.drawable.ic_category_unknown
)