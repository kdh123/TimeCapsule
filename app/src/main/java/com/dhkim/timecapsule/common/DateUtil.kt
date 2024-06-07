package com.dhkim.timecapsule.common

import android.annotation.SuppressLint
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
object DateUtil {

    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    fun millsToDate(mills: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.format(Timestamp(mills))

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun dateToMills(date: String): Long {
        val realDate = try {
            sdf.parse(date)
        } catch (e: Exception) {
            null
        }

        return realDate?.time ?: 0L
    }

    fun getDateGap(newDate: String): Long {
        return try {
            val afterDate = convertStringToDate(newDate)
            TimeUnit.DAYS.convert(afterDate!!.time - Calendar.getInstance().time.time, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            -1
        }
    }

    fun convertStringToDate(strDate: String, pattern: String = "yyyy-MM-dd"): Date? {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return try {
            format.parse(strDate)
        } catch (e: Exception) {
            null
        }
    }

    fun todayDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        return sdf.format(calendar.time)
    }

    fun dateAfterYears(years: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, years)

        return sdf.format(calendar.time)
    }

    fun dateAfterMonths(months: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, months)

        return sdf.format(calendar.time)
    }

    fun dateAfterDays(days: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, days)

        return sdf.format(calendar.time)
    }
}