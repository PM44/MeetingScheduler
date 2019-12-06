package com.example.meetingscheduler.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.meetingscheduler.R
import com.example.meetingscheduler.databinding.ListItemPotraitBinding
import com.example.meetingscheduler.model.Schedule


class ScheduleAdapter(val scheduleList: ArrayList<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ListItemPotraitBinding>(
            layoutInflater,
            R.layout.list_item_potrait, parent, false
        )
        return ScheduleViewHolder(view)
    }

    override fun getItemCount() = scheduleList.size


    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.view.schedule = scheduleList[position]
        var participantString = ""
        scheduleList.get(position).participants.forEachIndexed { i, participant ->
            if (scheduleList.get(position).participants.lastIndex == i) {
                participantString += participant
            } else {
                participantString += participant + ", "
            }

        }
        holder.view.participants.text = participantString
    }

    fun updateList(meetings: List<Schedule>?) {
        scheduleList.clear()
        meetings?.let { scheduleList.addAll(it) }
        notifyDataSetChanged()
    }

    class ScheduleViewHolder(var view: ListItemPotraitBinding) : RecyclerView.ViewHolder(view.root) {

    }

}