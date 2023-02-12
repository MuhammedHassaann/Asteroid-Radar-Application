package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    //method to get start date
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = Calendar.getInstance().time
        return dateFormat.format(currentDate)
    }

    //method to get end date
    fun getDateAfter7Days(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val dateAfter7Days = calendar.time
        return dateFormat.format(dateAfter7Days)
    }

}