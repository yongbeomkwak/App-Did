package com.example.appdid.bottomNavigation

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appdid.fragment.calendar.CalendarFragment
import com.example.appdid.fragment.todo.TodoFragment

class PagerAdapter(fm: FragmentManager, lc: Lifecycle):
    FragmentStateAdapter(fm, lc) {

    var fragmentList = arrayOfNulls<Fragment>(2)

    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        Log.e("WOW", "ADDED")
        if (position == 0) {
            fragmentList[0] = CalendarFragment()
            return fragmentList[0]!!
        }
        else if (position == 1) {
            fragmentList[1] = TodoFragment()
            return fragmentList[1]!!
        }
        else {
            error("No such position: $position")
        }
    }
}
