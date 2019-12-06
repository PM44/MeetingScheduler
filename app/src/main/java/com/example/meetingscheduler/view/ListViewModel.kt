package com.example.meetingscheduler.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.model.MeetingApiService
import com.example.meetingscheduler.model.Schedule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val meetingApiService = MeetingApiService()
    private val disposable = CompositeDisposable()
    val meetings = MutableLiveData<List<Schedule>>()
    val loading = MutableLiveData<Boolean>()

    fun fetchDataFromRemote(text: String) {
        loading.value = true
        disposable.add(
            meetingApiService.getSchedules(text)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Schedule>>() {
                    override fun onSuccess(t: List<Schedule>) {
                        meetings.value = t
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                    }

                })
        )

    }
}