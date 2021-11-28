package com.example.appdid.bottomNavigation

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Selected (view_pager2: ViewPager2, bottom_navi_view: BottomNavigationView){
    private var view_pager2: ViewPager2
    private var bottom_navi_view: BottomNavigationView

    init {
        this.view_pager2 = view_pager2
        this.bottom_navi_view = bottom_navi_view
    }

    inner class PageChangeCallback(function: () -> Unit) : ViewPager2.OnPageChangeCallback() { // ViewPage2 화면 전
        override fun onPageSelected(position: Int) { // 화면 전환시 호출되는 이벤환
            super.onPageSelected(position)
            bottom_navi_view.selectedItemId = when (position) {
                0 -> R.id.item_calendar
                1 -> R.id.item_todo
                else -> error("No such position $position")
            }
        }
    }
}