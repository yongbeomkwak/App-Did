package com.example.appdid.fragment.calendar.todo.week

import com.example.appdid.fragment.calendar.todo.day.CalendarDayTodoInfo
import java.util.*

class CalendarWeekTodoInfo (
    var beginCalendar: Calendar = Calendar.getInstance(), // 시작점 달력
    var endCalendar: Calendar = Calendar.getInstance(), // 끝점 달력
    var week: Int = 0, // 몇주차
    var height: Int = 0, // 높이
    var calendarDayTodoInfoWeek: MutableList<CalendarDayTodoInfo> =
        MutableList<CalendarDayTodoInfo>(0, init={ CalendarDayTodoInfo() }) // Todo List
        )