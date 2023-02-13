package com.udacity.asteroidradar.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {
    //method to get start date
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val today = LocalDate.now()
        return today.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

    //method to get end date
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateAfter7Days(): String {
        val nextWeek = LocalDate.now().plusDays(7)
        return nextWeek.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTomorrowDate(): String {
        val tomorrow = LocalDate.now().plusDays(1)
        return tomorrow.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

}