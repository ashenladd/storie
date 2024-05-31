package com.example.storie.core.utils

import com.example.storie.core.utils.DateConstant.DATE_MONTH_YEAR_FORMAT
import com.example.storie.core.utils.DateConstant.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private fun Long.formatDate(toFormat: String = DATE_MONTH_YEAR_FORMAT): String {
    val sdf = SimpleDateFormat(
        toFormat,
        Locale.getDefault()
    )
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(Date(this))
}

private fun String.parseDate(fromFormat: String = TIME_FORMAT): Long {
    val sdf = SimpleDateFormat(
        fromFormat,
        Locale.getDefault()
    )
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this)?.time ?: 0L
}

fun String.parseFormatDate(
    fromFormat: String = TIME_FORMAT,
    toFormat: String = DATE_MONTH_YEAR_FORMAT,
): String {
    return this.parseDate(fromFormat).formatDate(toFormat)
}