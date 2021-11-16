package com.example.appdid.fragment.calendar.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.example.appdid.R
import com.example.appdid.databinding.FragmentCalendarMonthBinding
import com.example.appdid.fragment.calendar.helper.Operate
import com.example.appdid.fragment.calendar.day.CalendarDayAdapter
import com.example.appdid.fragment.calendar.day.CalendarInfo
import com.example.appdid.fragment.calendar.todo.day.CalendarDayTodoInfo
import com.example.appdid.fragment.calendar.todo.week.CalendarWeekTodoAdapter
import com.example.appdid.fragment.calendar.todo.week.CalendarWeekTodoInfo
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

                var calendarWeekTodoAdapter = CalendarWeekTodoAdapter()
                binding.calendarTodoMonthRecycler.setHasFixedSize(true)
                binding.calendarTodoMonthRecycler.adapter = calendarWeekTodoAdapter
                initWeekTodo(calendarWeekTodoAdapter)
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
            dayList[i] = CalendarInfo()
            dayList[i].dayCalendar.set(bfYear, bfMonth-1, bfMaxDate - dayList.size + i + 1)
        }

        for (i in 1..maxDate) {
            dayList.add(
                CalendarInfo()
            )
            dayList.get(dayList.size - 1).dayCalendar.set(year, month-1, i)
        }

        val afCalendar = getMonth(year, month)
        val afMonth = afCalendar.get(Calendar.MONTH) + 1
        val afYear = afCalendar.get(Calendar.YEAR)
        var i = 1
        while(dayList.size % 7 != 0) {
            dayList.add(
                CalendarInfo()
            )
            dayList.get(dayList.size - 1).dayCalendar.set(afYear, afMonth-1, i++)
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
        return Operate.isSame(calendarInfo.dayCalendar, nowCalendar)
    }

    fun setListColor(list: MutableList<CalendarInfo>, month: Int) { // 달력 내 일이 일요일이면 글자가 빨간색, 당일이면 파란색, 다른달이면 배경이 회색
        for(i in 0 until list.size) {
            val calendarInfo = list.get(i)
            if (calendarInfo.dayCalendar.get(Calendar.DAY_OF_WEEK) == 1) {
                calendarInfo.textColor = R.color.calendar_red
            }

            if (isToday(calendarInfo)) {
                calendarInfo.textColor = R.color.calendar_blue
            }

            if (calendarInfo.dayCalendar.get(Calendar.MONTH)+1 != month) {
                calendarInfo.groundTrans = R.color.calendar_opaque
            }
        }
    }

    fun setListHeight() { // 각 날짜의 크기를 달력에 맞춤
        for(i in 0 until dayList.size) {
            dayList[i].height = (recyclerHeight / (dayList.size / 7))
        }
    }

    fun initWeekTodo(calendarWeekTodoAdapter: CalendarWeekTodoAdapter) { // Todo List를 표기해야할 주차별로 정리 후 해당 뷰로 전달
        var weekList = MutableList((dayList.size / 7), init = { CalendarWeekTodoInfo() })
        val todoList = getTodoList()

        for (i in 0 until weekList.size) {

            var calendarWeekTodoInfo = CalendarWeekTodoInfo(
                dayList.get(i * 7).dayCalendar,
                dayList.get((i + 1) * 7 - 1).dayCalendar,
                i,
                dayList[i*7].height)

            for (j in 0 until todoList.size) {
                if (!Operate.isAfter(todoList[j].beginCalendar, calendarWeekTodoInfo.endCalendar) &&
                        !Operate.isAfter(calendarWeekTodoInfo.beginCalendar, todoList[j].endCalendar)) {
                    val temp = todoList[j].copy()
                    if (Operate.isAfter(calendarWeekTodoInfo.beginCalendar, temp.beginCalendar)) {
                        temp.beginCalendar = calendarWeekTodoInfo.beginCalendar
                    }

                    if (Operate.isAfter(temp.endCalendar, calendarWeekTodoInfo.endCalendar)) {
                        temp.endCalendar = calendarWeekTodoInfo.endCalendar
                    }

                    calendarWeekTodoInfo.calendarDayTodoInfoWeek.add(temp)
                }
            }

            calendarWeekTodoInfo.calendarDayTodoInfoWeek.sortBy { it.beginCalendar }
            weekList[i] = calendarWeekTodoInfo
        }

        calendarWeekTodoAdapter.submitList(weekList)
    }

    fun getTodoList() : MutableList<CalendarDayTodoInfo> { // 서버에서 todoList가져옴
        var calendarDayTodoInfoList = MutableList(7, init={ CalendarDayTodoInfo() })
        var temp = calendarDayTodoInfoList.get(0)
        temp.title = "첫번째"
        temp.beginCalendar = Calendar.getInstance()
        temp.endCalendar = Calendar.getInstance()
        temp.endCalendar.add(Calendar.DAY_OF_MONTH, 1)
        temp._group = "그룹1"
        temp.id = 1

        val temp2 = calendarDayTodoInfoList.get(1)
        temp2.title = "두번째"
        temp2.beginCalendar = Calendar.getInstance()
        temp2.beginCalendar.add(Calendar.DAY_OF_MONTH, 1)
        temp2.endCalendar = Calendar.getInstance()
        temp2.endCalendar.add(Calendar.DAY_OF_MONTH, 1)

        val temp3 = calendarDayTodoInfoList.get(2)
        temp3.title = "세번째"
        temp3.beginCalendar = Calendar.getInstance()
        temp3.beginCalendar.add(Calendar.DAY_OF_MONTH, 2)
        temp3.endCalendar = Calendar.getInstance()
        temp3.endCalendar.add(Calendar.DAY_OF_MONTH, 2)

        val temp4 = calendarDayTodoInfoList.get(3)
        temp4.title = "네번째"
        temp4.beginCalendar = Calendar.getInstance()
        temp4.beginCalendar.add(Calendar.DAY_OF_MONTH, 3)
        temp4.endCalendar = Calendar.getInstance()
        temp4.endCalendar.add(Calendar.DAY_OF_MONTH, 3)

        val temp5 = calendarDayTodoInfoList.get(4)
        temp5.title = "다섯번째"
        temp5.beginCalendar = Calendar.getInstance()
        temp5.beginCalendar.add(Calendar.DAY_OF_MONTH, 4)
        temp5.endCalendar = Calendar.getInstance()
        temp5.endCalendar.add(Calendar.DAY_OF_MONTH, 5)

        val temp6 = calendarDayTodoInfoList.get(5)
        temp6.title = "여섯번째게는전부붙혀서좀길"
        temp6.beginCalendar = Calendar.getInstance()
        temp6.beginCalendar.add(Calendar.DAY_OF_MONTH, 5)
        temp6.endCalendar = Calendar.getInstance()
        temp6.endCalendar.add(Calendar.DAY_OF_MONTH, 6)

        val temp7 = calendarDayTodoInfoList.get(6)
        temp7.title = "일곱번째는 완전 길게길게길게길게길"
        temp7.beginCalendar = Calendar.getInstance()
        temp7.beginCalendar.add(Calendar.DAY_OF_MONTH, 0)
        temp7.endCalendar = Calendar.getInstance()
        temp7.endCalendar.add(Calendar.DAY_OF_MONTH, 7)
        return calendarDayTodoInfoList
    }
}