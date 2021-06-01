package com.example.moneykeeper3

import android.util.Log
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

const val DATE_PATTERN_DB = "yyyyMMddHHmmss"
const val DATE_PATTERN_PARSE = "yyyyMMddHHmmss VV"
const val DATE_PATTERN_PRETTY_DETAIL = "dd-MMM-yyyy h:mma zz"
const val TIME_PATTERN_PRETTY = "h:mm a."
const val DATE_PATTERN_PRETTY = "MMM. dd, yyyy"
const val TIME_ZONE_DB = "America/New_York"



/**
 * Convert date to strings with zone of TIME_ZONE_DB = "America/Toronto"
 */
fun convertCalendarForRepo(localDateTime: LocalDateTime): String {
    return DateTimeFormatter.ofPattern(DATE_PATTERN_DB).format(
            ZonedDateTime
                    .of(localDateTime,ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of(TIME_ZONE_DB)))
}
/**
 * parse db date assuming timezone store is TIME_ZONE_DB = "America/Toronto"
 */
fun getDateFromRepo(date: String): ZonedDateTime{
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN_PARSE)
    return ZonedDateTime.parse("$date $TIME_ZONE_DB", formatter).withZoneSameInstant(ZoneId.systemDefault())
}

fun formatDatePretty(zoneDateTime: ZonedDateTime): String{
    return DateTimeFormatter.ofPattern(DATE_PATTERN_PRETTY).format(zoneDateTime)
}

fun formatPrettyDateDetail(zoneDateTime: ZonedDateTime): String{
    return DateTimeFormatter.ofPattern(DATE_PATTERN_PRETTY_DETAIL).format(zoneDateTime)
}

fun formatTimePretty(time: LocalTime): String{
    return DateTimeFormatter.ofPattern(TIME_PATTERN_PRETTY).format(time).toLowerCase(Locale.getDefault())
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


