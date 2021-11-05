package com.example.appdid.fragment.calendar.day

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.databinding.LayoutCalendarDayBinding

class CalendarDayAdapter : ListAdapter<CalendarInfo, CalendarDayAdapter.ViewHolder>( //달력 일 어뎁터
    CalendarAdapterDiffCallback()
) {

    class ViewHolder private constructor(
        val binding: LayoutCalendarDayBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarInfo) {
            binding.calendarInfo = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutCalendarDayBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class CalendarAdapterDiffCallback: DiffUtil.ItemCallback<CalendarInfo>() {
    override fun areItemsTheSame(oldItem: CalendarInfo, newItem: CalendarInfo): Boolean {
        return oldItem.dayOfMonth == newItem.dayOfMonth
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: CalendarInfo, newItem: CalendarInfo): Boolean {
        return oldItem == newItem
    }

}