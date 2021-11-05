package com.example.appdid.fragment.calendar.month

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarMonthPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    val YEAR_RANGE = 20
    var calendarMonthFragments = arrayOfNulls<CalendarMonthFragment>(YEAR_RANGE * 12)

    override fun getItemCount(): Int {
        return YEAR_RANGE * 12
    }

    override fun createFragment(position: Int): Fragment {
        var calendarMonthFragment = CalendarMonthFragment()
        calendarMonthFragments[position] = calendarMonthFragment
        return calendarMonthFragment
    }

    fun getItem(position: Int) : CalendarMonthFragment? {
        return calendarMonthFragments[position]
    }
}