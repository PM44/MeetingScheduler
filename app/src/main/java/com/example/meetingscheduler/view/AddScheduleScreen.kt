package com.example.meetingscheduler.view

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.meetingscheduler.R
import com.example.meetingscheduler.databinding.FragmentAddScheduleBinding
import com.example.meetingscheduler.viewmodel.AddScheduleViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.util.*


class AddScheduleScreen : Fragment() {
    private lateinit var databinding: FragmentAddScheduleBinding
    private lateinit var viewModel: AddScheduleViewModel
    lateinit var progress: ProgressDialog
    var startHour: Int = -1
    var startMinute: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_schedule, container, false
        )
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        previous_day.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(AddScheduleScreenDirections.actionAddScheduleToListViewFragment())
        }
        databinding.setDatePicker {
            setCalenderDate()
        }

        databinding.setStartTimePicker {
            startTimeDatePicker(it)
        }

        databinding.setEndTimePicker {
            endTimeDatePicker(it)
        }
        viewModel = ViewModelProviders.of(this).get(AddScheduleViewModel::class.java)

        progress = ProgressDialog(context!!).apply {
            setMessage("Loading")
        }


        observeViewModel()

        submit.setOnClickListener {

            if (!schedule_date.text.toString().isNullOrEmpty() &&
                !schedule_start_time.text.toString().isNullOrEmpty() &&
                !schedule_end_time.text.toString().isNullOrEmpty() &&
                !schedule_description.text.toString().trim().isNullOrEmpty()
            ) {
                if (schedule_start_time.text.toString() == schedule_end_time.text.toString()) {
                    Toast.makeText(activity, "Start time and end time cannot be same", Toast.LENGTH_SHORT).show();
                } else {
                    progress.show()
                    viewModel.fetchDataFromRemote(schedule_date.text.toString())
                }
            } else {
                Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private fun setup() {
        previous_text.text = "BACK"
        next_day.visibility = View.GONE
        date.text = "SCHEDULE A MEETING"
    }


    private fun observeViewModel() {
        viewModel.meetings.observe(this, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                progress.hide()
                val isAvailable = viewModel.getSlotObservation(
                    schedule_start_time.text.toString(),
                    schedule_end_time.text.toString(), it
                )

                val text = if (isAvailable) "Slot Available" else "Slot not Available"
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                viewModel.meetings.value = listOf()
            }
        })
    }

    fun setCalenderDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd =
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                schedule_date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            }, year, month, day)
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
        dpd.show()

    }

    fun startTimeDatePicker(view: View) {
        schedule_end_time.text=""
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            startHour = h
            startMinute = m
            if (h < 9 || h > 18) {
                Toast.makeText(activity, "Office timings are between 9:00A.M to 6:00 P.M", Toast.LENGTH_SHORT).show();
            } else {
                if (m % 30 == 0 || m % 60 == 0)
                    schedule_start_time.text = h.toString() + " : " + m.toString()
                else {
                    Toast.makeText(activity, "Please Select time in multiple of 30 minutes", Toast.LENGTH_SHORT).show();
                }
            }
        }), hour, minute, true)
        tpd.show()
    }

    fun endTimeDatePicker(view: View) {
        if (!schedule_start_time.text.toString().trim().isNullOrEmpty()) {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                if (h < 9 || h > 18) {
                    Toast.makeText(activity, "Office timings are between 9:00A.M to 6:00 P.M", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    if (startHour <= h) {
                        if ((startHour == h) && (startMinute > m)) {
                            Toast.makeText(activity, "Start time should be less than end time", Toast.LENGTH_SHORT)
                                .show();
                        } else {
                            if (m % 30 == 0 || m % 60 == 0)
                                schedule_end_time.text = h.toString() + " : " + m.toString()
                            else {
                                Toast.makeText(
                                    activity,
                                    "Please Select time in multiple of 30 minutes",
                                    Toast.LENGTH_SHORT
                                )
                                    .show();
                            }
                        }
                    } else {
                        Toast.makeText(activity, "Start time should be less than end time", Toast.LENGTH_SHORT)
                            .show();
                    }
                }


            }), hour, minute, true)


            tpd.show()
        } else {
            Toast.makeText(activity, "Please Select start time first", Toast.LENGTH_SHORT)
                .show();
        }
    }
}
