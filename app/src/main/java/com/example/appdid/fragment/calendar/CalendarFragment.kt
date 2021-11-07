package com.example.appdid.fragment.calendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.appdid.DTO.MyTodoDTO
import com.example.appdid.DTO.MyTodoListDTO
import com.example.appdid.DTO.TestDto
import com.example.appdid.RetrofitSet.RetrofitCreator
import com.example.appdid.RetrofitSet.RetrofitService
import com.example.appdid.bottomNavigation.Selected
import com.example.appdid.databinding.FragmentCalendarBinding
import com.example.appdid.fragment.calendar.month.CalendarMonthPagerAdapter
import com.example.appdid.utility.ServerUri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class CalendarFragment : Fragment() {   // 달력 Fragment
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CalendarMonthPagerAdapter
    private lateinit var pageChangeCallback: PageChangeCallback //달력 달이 바뀔 때 호출

    lateinit var monthText: TextView // 해당 달력이 무슨 날짜의 달력인지 표시하는 TextView
    lateinit var calendarMonthViewpager2: ViewPager2 // 달력 달 ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        monthText = binding.monthText

        adapter = CalendarMonthPagerAdapter(parentFragmentManager, lifecycle)
        pageChangeCallback = PageChangeCallback()
        calendarMonthInit()

        return root
    }

    override fun onStart() {
        super.onStart()

        getServer()
        startCalendarMonthSetting()
    }

    private fun getServer() {
        val retrofit: Retrofit = RetrofitCreator.defaultRetrofit(ServerUri.MyServer) //빌더
        val service:RetrofitService=retrofit.create(RetrofitService::class.java)//인터페이스
        val call:Call<MyTodoListDTO> =service.getMyTodoList("123412341234") //해당 인터페이스를 통한 RUST 접근
        call.enqueue(object:Callback<MyTodoListDTO>{
            override fun onResponse(call: Call<MyTodoListDTO>, response: Response<MyTodoListDTO>) {
                if(response.isSuccessful)
                {
                    val myTodoList: MyTodoListDTO =response.body()!!

//                    Log.e("RES",myTodoList.payloads[0].title)
                }
                else
                {
                    Log.e("RES",response.toString())
                }
            }

            override fun onFailure(call: Call<MyTodoListDTO>, t: Throwable) {
                Log.e("RES",t.message.toString())
            }
        })
    }

    private fun startCalendarMonthSetting() { // 진입시 달력을 현재 달로 불러옴
        var month: Int = Calendar.getInstance().get(Calendar.MONTH)
        calendarMonthViewpager2.setCurrentItem(adapter.itemCount / 2 + month, true)
        while(true) {
            try{
                Handler().postDelayed({
                    pageChangeCallback.onPageSelected((adapter.itemCount / 2) + month)
                }, 1000)
                break
            } catch (e: Exception) { }
        }
    }

    private fun calendarMonthInit() { // 달력 Init
        calendarMonthViewpager2 = binding.calendarMonthViewpager2
        calendarMonthViewpager2.adapter = adapter

        calendarMonthViewpager2.registerOnPageChangeCallback(pageChangeCallback)
    }

    inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() { // 달력 달이 바뀔 경우 달 전환
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            var year = Calendar.getInstance().get(Calendar.YEAR) + (position / 12) - 10
            var month = position % 12
            setMonthText(year, month+1)

            adapter.getItem(position)?.initCalendar(year, month)
        }
    }

    fun setMonthText(month: String) { // 상단 날짜 TextView 글자 지정
        monthText.setText(month)
    }

    fun setMonthText(year: Int, month: Int) { // 상단 날짜 TextView 글자 지정
        monthText.setText(year.toString() + "년 " + month.toString() + "월")
    }

}
