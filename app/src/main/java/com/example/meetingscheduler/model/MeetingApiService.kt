package com.example.meetingscheduler.model

import android.os.Build
import com.example.meetingscheduler.BuildConfig
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MeetingApiService {
    private val BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/"

    val interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MeetingApi::class.java)

    fun getSchedules(date:String): Single<List<Schedule>> {
        return api.getMeetings(date)
    }
}