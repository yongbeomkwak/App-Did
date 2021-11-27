package com.example.appdid.fragment.calendar.todo.day

import com.example.appdid.R
import java.util.*

class CalendarDayTodoInfo (
    var id: String = "", // 할일 id
    var author: String = "", // 작성자
    var beginCalendar: Calendar = Calendar.getInstance(), // 시작 달력
    var endCalendar: Calendar = Calendar.getInstance(), // 끝 달력
    var title: String = "title", // Todo 이름
    var width: Int = 0, // 넓이
    var _project_id: String = "", // 프로젝트 id
    var color: String = "#FFFFFF", // 배경색
    var check: Boolean = false, // 체크
    var row: Int = -1, // 몇번째 행
        ) {
    fun copy() : CalendarDayTodoInfo { // deep copy method
        return CalendarDayTodoInfo(id, author, beginCalendar, endCalendar, title, width, _project_id, color, check, -1)
    }
}