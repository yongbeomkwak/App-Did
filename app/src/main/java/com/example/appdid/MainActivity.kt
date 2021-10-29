package com.example.appdid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 일일이 R.id.*를 하지 않기 위한 바인딩
    private lateinit var view_pager2: ViewPager2 // 달력, todo리스트 화면전환을 위한 ViewPager2
    private lateinit var bottom_navi_view: BottomNavigationView // 화면전환 컨트롤을 위한 Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appbarView:View=findViewById(R.id.incAppBar) as View //include 태그 View를 가져오기 위함
        val appBar:MaterialToolbar=appbarView.findViewById(R.id.appBar) as MaterialToolbar //include View에서 실제 appBar가져옴
        setSupportActionBar(appBar) //ActionBar 등록

        viewPager2Init()
        setExpandableList()

        appBar.setNavigationOnClickListener { //햄버거 메뉴 클릭 이벤트
            if(binding.dlContainer.isDrawerOpen(GravityCompat.START))
            {
                binding.dlContainer.closeDrawer(GravityCompat.START)
            }
            else
            {
                binding.dlContainer.openDrawer(GravityCompat.START)
            }
        }
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

    private fun  setExpandableList() //리스트 이니셜라이저
    {
        val parentsList= mutableListOf<String>("부모1","부모2","부모3")
        val childList = mutableListOf( mutableListOf(), mutableListOf("자식 1", "자식 2"), mutableListOf("자식 1", "자식 2", "자식 3") )
        val expandableListAdapter=com.example.appdid.expandableList.ExpandableListAdapter(this,parentsList,childList)
        binding.elMenu.setAdapter(expandableListAdapter)
        binding.elMenu.setOnGroupClickListener { parent, v, groupPosition, id ->
            //TODO
            println("Click group")
            false
        }
        binding.elMenu.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //TODO
            println("Click child")
            false
        }

    }
}