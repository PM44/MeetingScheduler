package com.example.meetingscheduler.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meetingscheduler.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
    }
}
