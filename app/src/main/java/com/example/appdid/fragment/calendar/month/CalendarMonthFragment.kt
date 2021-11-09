package com.example.appdid.fragment.calendar.month

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.example.appdid.R
import com.example.appdid.databinding.FragmentCalendarMonthBinding
import com.example.appdid.fragment.calendar.day.CalendarDayAdapter
import com.example.appdid.fragment.calendar.day.CalendarInfo
import java.util.*

class CalendarMonthFragment(var year: Int, var month: Int) : Fragment() { //Calendar Fragment에서 사용하는 달력 달 Fragment
    private lateinit var _binding: FragmentCalendarMonthBinding
    private val binding get() = _binding!!

    lateinit var nowCalendar: Calendar // 실제 날짜
    lateinit var calendar: Calendar // 사용자가 보는 달력의 날짜

    lateinit var dayList: MutableList<CalendarInfo> //각 날짜 배열
    var recyclerHeight = 0 // 달력 높이

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarMonthBinding.inflate(inflater, container, false)
        var root: View = binding.root

        var calendarDayAdapter = CalendarDayAdapter()
        binding.calendarMonthRecycler.setHasFixedSize(true)
        binding.calendarMonthRecycler.adapter = calendarDayAdapter

        binding.calendarMonthRecycler.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener { // 달력의 크기를 가져오기 위한 리스너
            override fun onGlobalLayout() {
                binding.calendarMonthRecycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                recyclerHeight = binding.calendarMonthRecycler.height
                initCalendar(calendarDayAdapter)
            }
        })

        return root
    }

    fun initCalendar(calendarDayAdapter: CalendarDayAdapter) { // 달력 init
        nowCalendar = Calendar.getInstance()
        calendar=  Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        calendar.set(year, month, 1)

        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        dayList = MutableList(week, init={ CalendarInfo() })

        val bfCalendar = getMonth(year, month-2)
        val bfMaxDate = bfCalendar.getActualMaximum(Calendar.DATE)
        val bfMonth = bfCalendar.get(Calendar.MONTH) + 1
        val bfYear = bfCalendar.get(Calendar.YEAR)
        for(i in 0 until dayList.size) {
            dayList[i] = CalendarInfo(bfYear, bfMonth, bfMaxDate - dayList.size + i + 1, i % 7)
        }

        for (i in 1..maxDate) {
            dayList.add(
                CalendarInfo(year, month, i, (week + i - 1) % 7)
            )
        }

        val afCalendar = getMonth(year, month)
        val afMonth = afCalendar.get(Calendar.MONTH) + 1
        val afYear = afCalendar.get(Calendar.YEAR)
        var i = 1
        while(dayList.size % 7 != 0) {
            dayList.add(
                CalendarInfo(afYear, afMonth, i++, dayList.size % 7)
            )
        }

        setListColor(dayList, month)

        calendarDayAdapter.submitList(dayList)
        setListHeight()
    }

    fun getMonth(year: Int, month: Int) : Calendar { // 해당 년도와 달의 달력을 리턴
        var _year = year
        var _month = month
        var calendar: Calendar = Calendar.getInstance()

        if (month < 0) {
            _month = 11
            _year -= 1
        } else if (month > 11) {
            _month = 0
            _year += 1
        }

        calendar.set(Calendar.YEAR, _year)
        calendar.set(Calendar.MONTH, _month)
        return calendar
    }

    fun isToday(calendarInfo: CalendarInfo) : Boolean { // 해당 일이 실제 오늘인가
        return calendarInfo.year == nowCalendar.get(Calendar.YEAR) &&
                calendarInfo.month == nowCalendar.get(Calendar.MONTH) + 1 &&
                calendarInfo.dayOfMonth == nowCalendar.get(Calendar.DAY_OF_MONTH)
    }

    fun setListColor(list: MutableList<CalendarInfo>, month: Int) { // 달력 내 일이 일요일이면 글자가 빨간색, 당일이면 파란색, 다른달이면 배경이 회색
        for(i in 0 until list.size) {
            val calendarInfo = list.get(i)
            if (calendarInfo.dayOfWeek == 0) {
                calendarInfo.textColor = R.color.calendar_red
            }

            if (isToday(calendarInfo)) {
                calendarInfo.textColor = R.color.calendar_blue
            }

            if (calendarInfo.month != month) {
                calendarInfo.groundTrans = R.color.calendar_opaque
            }
        }
    }

    fun setListHeight() { // 각 날짜의 크기를 달력에 맞춤
        for(i in 0 until dayList.size) {
            dayList[i].height = (recyclerHeight / (dayList.size / 7))
        }
    }
}