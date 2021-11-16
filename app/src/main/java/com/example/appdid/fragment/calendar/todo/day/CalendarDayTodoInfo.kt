package com.example.appdid.fragment.calendar.todo.day

import com.example.appdid.R
import java.util.*

class CalendarDayTodoInfo (
    var id: Int = 0, // 할일 id
    var beginCalendar: Calendar = Calendar.getInstance(), // 시작 달력
    var endCalendar: Calendar = Calendar.getInstance(), // 끝 달력
    var title: String = "title", // Todo 이름
    var width: Int = 0, // 넓이
    var _group: String = "", // Todo 그룹 이름
    var color: Int = R.color.purple_200, // 배경색
    var row: Int = -1, // 몇번째 행
        ) {
    fun copy() : CalendarDayTodoInfo { // deep copy method
        return CalendarDayTodoInfo(id, beginCalendar, endCalendar, title, width, _group, color, -1)
    }
}