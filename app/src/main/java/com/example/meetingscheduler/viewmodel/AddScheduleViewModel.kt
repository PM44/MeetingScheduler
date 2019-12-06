package com.example.meetingscheduler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.model.Schedule
import com.example.meetingscheduler.model.MeetingApiService
import com.example.meetingscheduler.util.Converter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val meetingApiService = MeetingApiService()
    private val disposable = CompositeDisposable()
    var slots = IntArray(18)
    val meetings = MutableLiveData<List<Schedule>>()
    // val reponseSuccess = MutableLiveData<List<Schedule>>()

    fun fetchDataFromRemote(date: String) {
        disposable.add(
            meetingApiService.getSchedules(date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Schedule>>() {
                    override fun onSuccess(t: List<Schedule>) {
                        slots.fill(0, 0, 17)
                        val sortedMeetings = t?.sortedWith(compareBy({ it.start_time }))
                        if (!sortedMeetings.isNullOrEmpty()) {
                            meetings.value = sortedMeetings
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })
        )

    }


    fun getSlotObservation(startTime: String, endTime: String, schedules: List<Schedule>): Boolean {
        schedules?.forEachIndexed { r, x ->
            var t1 = (Converter.returnIndex(Converter.parseTimeToMinutes(x.start_time))).toInt()
            var t2 = (Converter.returnIndex(Converter.parseTimeToMinutes(x.end_time))).toInt()
            for (i in t1 until t2) {
                slots[i] = 1
            }
        }

        var isAvailable = true
        var t1 = (Converter.returnIndex(Converter.parseTimeToMinutes(startTime))).toInt()
        var t2 = (Converter.returnIndex(Converter.parseTimeToMinutes(endTime))).toInt()
        for (i in t1 until t2) {
            if (slots[i] == 1) {
                isAvailable = false
                break
            }
        }
        return isAvailable
    }
}