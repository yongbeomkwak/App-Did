package com.example.appdid.fragment.calendar.month

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class CalendarMonthPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    val YEAR_RANGE = 20 // 달력 범위 (현재 년도를 기준으로 20년)
    var calendarMonthFragments = arrayOfNulls<CalendarMonthFragment>(YEAR_RANGE * 12)

    override fun getItemCount(): Int { // 달 갯수 리턴
        return YEAR_RANGE * 12
    }

    override fun createFragment(position: Int): Fragment { // 달력의 position번째 달을 만들 때 호출
        if (calendarMonthFragments[position] == null) {
            var year = Calendar.getInstance().get(Calendar.YEAR) + (position / 12) - 10
            var month = position % 12
            var calendarMonthFragment = CalendarMonthFragment(year, month)
            calendarMonthFragments[position] = calendarMonthFragment
        }
        return calendarMonthFragments[position]!!
    }
}