package com.example.meetingscheduler.model

import com.google.gson.annotations.SerializedName

data class JobScheduler(val jsonList : List<Schedule>)

data class Schedule (

    @SerializedName("start_time")
    val start_time : String,
    @SerializedName("end_time")
    val end_time : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("participants")
    val participants : List<String>
)