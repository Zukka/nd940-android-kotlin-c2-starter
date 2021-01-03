package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import java.text.SimpleDateFormat
import java.util.*

private const val dayInAWeek = 7

fun getToday(): String {
    val calendar = Calendar.getInstance()
    val df = SimpleDateFormat(API_QUERY_DATE_FORMAT);
    return df.format(calendar.time)
}

fun getDatePlusAWeek() : String {
    val calendar = Calendar.getInstance()
    calendar.add(DEFAULT_END_DATE_DAYS, Calendar.MONDAY)
    val df = SimpleDateFormat(API_QUERY_DATE_FORMAT);
    return df.format(calendar.time)
}
