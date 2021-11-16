package com.example.appdid.fragment.calendar.day

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appdid.databinding.LayoutCalendarDayBinding
import java.util.*

class CalendarDayAdapter : ListAdapter<CalendarInfo, CalendarDayAdapter.ViewHolder>( //달력 일 어뎁터
    CalendarAdapterDiffCallback()
) {

    lateinit var viewHolder: ViewHolder

    class ViewHolder private constructor( // RecyclerView 사용을 위한 Holder.
        val binding: LayoutCalendarDayBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarInfo) { // 달력 일과 CalendarInfo 바인딩
            binding.calendarInfo = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder { // ViewBinding
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutCalendarDayBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // 달력의 일이 만들어졌을 때 호출
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // ViewHolder에 position번째 CalendarInfo 바인딩
        val item = getItem(position)
        holder.bind(item)
        viewHolder = holder
    }

    companion object {
        @JvmStatic
        @BindingAdapter("tools:layout_height")
        fun setLayoutHeight(view: View, height: Int) { // 달력 내 날짜의 높이를 calendarInfo의 height로 조절
            val layoutParams = view.layoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
            view.invalidate()
        }

        @JvmStatic
        @BindingAdapter("tools:text")
        fun setTextviewText(view: TextView, calendar: Calendar) {   // 달력 날짜 설정
            view.setText(calendar.get(Calendar.DAY_OF_MONTH).toString())
        }
    }

}

class CalendarAdapterDiffCallback: DiffUtil.ItemCallback<CalendarInfo>() { // RecyclerView 전환시 두 목록의 차이점을 찾아 업데이트
    override fun areItemsTheSame(oldItem: CalendarInfo, newItem: CalendarInfo): Boolean {
        return oldItem.dayCalendar.get(Calendar.DAY_OF_MONTH) == newItem.dayCalendar.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: CalendarInfo, newItem: CalendarInfo): Boolean {
        return oldItem == newItem
    }

}