package com.example.moneykeeper3

import android.icu.util.Calendar
import com.example.moneykeeper3.database.Category
import java.time.*
import java.time.format.DateTimeFormatter

const val DATE_PATTERN_DB = "yyyyMMddHHmmss"
const val DATE_PATTERN_PARSE = "yyyyMMddHHmmss VV"
const val TIME_ZONE_DB = "America/New_York"

/**
 * Convert date to strings with zone of TIME_ZONE_DB = "America/Toronto"
 */
fun convertCalendarForRepo(localDateTime: LocalDateTime): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN_DB)
    return formatter.format(ZonedDateTime.of(localDateTime,ZoneId.of(TIME_ZONE_DB)))
}
/**
 * parse db date assuming timezone store is TIME_ZONE_DB = "America/Toronto"
 */
fun parseCalendarFromRepo(date: String): ZonedDateTime{
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN_PARSE)
    return ZonedDateTime.parse("$date $TIME_ZONE_DB", formatter).withZoneSameInstant(ZoneId.systemDefault())
}

data class DateRange(val startDate: LocalDateTime, val endDate: LocalDateTime)

fun getDateOfDay(day: Int): DateRange{
    val tempDate = if (day == 0) LocalDate.now() else LocalDate.now().withDayOfMonth(day)
    return getDateBetween(tempDate, tempDate)
}

fun getDateOfMonth(month: Int): DateRange {
    val tempDate = if (month == 0) LocalDate.now() else LocalDate.now().withMonth(month)
    val startDate = tempDate.withDayOfMonth(1)
    val endDate = tempDate.withDayOfMonth(tempDate.lengthOfMonth())
    return getDateBetween(startDate, endDate)
}

fun getDateBetween(start: LocalDate, end:LocalDate): DateRange {
    return DateRange(start.atStartOfDay(), end.atTime(LocalTime.MAX))
}

val defaultCategory = listOf(
        Category("Unknown",0x8eacbb, "ic_unknown"), // This is the Default when get deleted
        Category("Default",0xcfcfcf, "ic_default"),
        Category("Clothing",0xa98274, "ic_clothing"),
        Category("Entertainment",0x6ec6ff, "ic_entertainment"),
        Category("Food",0xbef67a, "ic_food"),
        Category("Gift",0xff6090, "ic_gift"),
        Category("Grocery",0xff8a50, "ic_grocery"),
        Category("Income",0x80e27e, "ic_income"),
        Category("Rent",0xff7961, "ic_rent"),
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