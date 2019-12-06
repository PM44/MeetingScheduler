package com.example.meetingscheduler.util

import java.lang.Exception


object Converter {
    fun parseTimeToMinutes(hourFormat: String): Double {
        var minutes = 0.0
        val split = hourFormat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return try {
            minutes += java.lang.Double.parseDouble(split[0]) * 60
            minutes += java.lang.Double.parseDouble(split[1])
            minutes
        } catch (e: Exception) {
            -1.0
        }
    }

    fun  returnIndex(t:Double):Double {
        return (t-540.0)/30.0
    }
}