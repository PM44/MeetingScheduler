package com.example.meetingscheduler.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetingscheduler.R
import com.example.meetingscheduler.view.ListViewModel
import com.example.meetingscheduler.view.ScheduleAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_list_view.*
import java.text.SimpleDateFormat
import java.util.*


class ListViewFragment : Fragment() {
    private val scheduleAdapter = ScheduleAdapter(arrayListOf())
    private lateinit var viewModel: ListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy")
        date.text = df.format(c.time)
        val calenderDate = df.parse(date.text.toString())
        c.time = calenderDate

        previous_day.setOnClickListener {
            c.add(Calendar.DAY_OF_MONTH, -1)
            date.text = df.format(c.time)
            viewModel.fetchDataFromRemote(date.text.toString())
        }

        next_day.setOnClickListener {
            c.add(Calendar.DAY_OF_MONTH, 1)
            date.text = df.format(c.time)
            viewModel.fetchDataFromRemote(date.text.toString())
        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.fetchDataFromRemote(date.text.toString())
        schedule_meeting.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(ListViewFragmentDirections.actionListViewFragmentToAddSchedule())
        }

        observeViewModel()

        schedule_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleAdapter
        }

    }

    private fun observeViewModel() {

        viewModel.meetings.observe(this, androidx.lifecycle.Observer { meetings ->
            val sortedMeetings = meetings?.sortedWith(compareBy({ it.start_time }))
            sortedMeetings.let {
                scheduleAdapter.updateList(sortedMeetings)
            }
        })


        viewModel.loading.observe(this, androidx.lifecycle.Observer { loading ->
            if (loading) {
                loadingView.visibility = View.VISIBLE
            } else {
                loadingView.visibility = View.GONE
            }

        })
    }

}
