package com.example.appdid.bottomNavigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appdid.fragment.calendar.CalendarFragment
import com.example.appdid.fragment.todo.TodoFragment

class PagerAdapter(fm: FragmentManager, lc: Lifecycle):
    FragmentStateAdapter(fm, lc) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CalendarFragment()
            1 -> TodoFragment()
            else -> error("No such position: $position")
        }
    }
}
