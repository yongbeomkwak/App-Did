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
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog

class CalendarFragment : Fragment() {   // 달력 Fragment
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CalendarMonthPagerAdapter
    private lateinit var pageChangeCallback: PageChangeCallback //달력 달이 바뀔 때 호출

    lateinit var root: View // 메인 뷰
    lateinit var monthText: TextView // 해당 달력이 무슨 날짜의 달력인지 표시하는 TextView
    lateinit var calendarMonthViewpager2: ViewPager2 // 달력 달 ViewPager2

    var year = 0
    var month = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        root = binding.root

        monthText = binding.monthText
        monthText.setOnClickListener(View.OnClickListener {
            showDatePickerDialog()
        })

        adapter = CalendarMonthPagerAdapter(parentFragmentManager, lifecycle)
        pageChangeCallback = PageChangeCallback()
        calendarMonthInit()

        return root
    }

    override fun onStart() {
        super.onStart()

//        getServer()
        var calendar = Calendar.getInstance()
        calendarYearMonthSetting(calendar)
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

    fun calendarYearMonthSetting(calendar: Calendar) { // 해당 날짜 달력을 보여줌
        var nowCalendar = Calendar.getInstance()
        var month: Int = calendar.get(Calendar.MONTH)
        var year: Int = calendar.get(Calendar.YEAR)
        calendarMonthViewpager2.setCurrentItem(((year + 10) - nowCalendar.get(Calendar.YEAR)) * 12 + month, true)
        pageChangeCallback.onPageSelected(((year + 10) - nowCalendar.get(Calendar.YEAR)) * 12 + month)
    }

    private fun calendarMonthInit() { // 달력 Init
        calendarMonthViewpager2 = binding.calendarMonthViewpager2
        calendarMonthViewpager2.adapter = adapter

        calendarMonthViewpager2.registerOnPageChangeCallback(pageChangeCallback)
    }

    inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() { // 달력 달이 바뀔 경우 달 전환
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            year = Calendar.getInstance().get(Calendar.YEAR) + (position / 12) - 10
            month = position % 12
            setMonthText(year, month+1)
        }
    }

    fun setMonthText(year: Int, month: Int) { // 상단 날짜 TextView 글자 지정
        monthText.setText(year.toString() + "년 " + month.toString() + "월 ▾")
    }

    fun showDatePickerDialog() { // 화면에 날짜 Picker 띄우기
        var nowCalendar = Calendar.getInstance()
        val nowYear = nowCalendar.get(Calendar.YEAR)
        val nowMonth = nowCalendar.get(Calendar.MONTH)
        var calendar = Calendar.getInstance()
        calendar.set(nowYear - 10, 0, 1)
        val minDate = calendar.timeInMillis
        calendar.clear()
        calendar.set(nowYear + 9, 11, 1)
        val maxDate = calendar.timeInMillis

        var dialogFragment: MonthYearPickerDialogFragment =
            MonthYearPickerDialogFragment.getInstance(nowMonth, nowYear, minDate, maxDate)
        dialogFragment.show(parentFragmentManager, null)
        dialogFragment.setOnDateSetListener { tyear, monthOfYear ->
            calendar.clear()
            calendar.set(tyear, monthOfYear, 1)
            calendarYearMonthSetting(calendar)
        }
    }

}
