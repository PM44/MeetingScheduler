package com.example.meetingscheduler.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetingApi {

    @GET("schedule")
    fun getMeetings(@Query("date") date:String?): Single<List<Schedule>>
}