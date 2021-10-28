package com.example.appdid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 일일이 R.id.*를 하지 않기 위한 바인딩
    private lateinit var view_pager2: ViewPager2 // 달력, todo리스트 화면전환을 위한 ViewPager2
    private lateinit var bottom_navi_view: BottomNavigationView // 화면전환 컨트롤을 위한 Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager2Init()
    }

    fun viewPager2Init() {   // ViewPager2 이니셜라이저
        view_pager2 = binding.viewPager
        view_pager2.adapter =
            com.example.appdid.bottomNavigation.PagerAdapter(supportFragmentManager, lifecycle)

        bottomNaviInit()
        val page_listener = Selected(view_pager2, bottom_navi_view)

        view_pager2.registerOnPageChangeCallback(page_listener.PageChangeCallback())
        view_pager2.setUserInputEnabled(false)
    }

    fun bottomNaviInit() {  // Bottom navigation view 이니셜라이저
        bottom_navi_view = binding.bottomNaviView
        bottom_navi_view.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.item_calendar -> {
                        view_pager2.currentItem = 0
                    }
                    R.id.item_todo -> {
                        view_pager2.currentItem = 1
                    }
                }
                true
            }
        }
    }
}